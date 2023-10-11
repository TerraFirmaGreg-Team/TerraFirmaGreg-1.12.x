package su.terrafirmagreg.util.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleRegistry {

    private final List<ModuleBase> moduleList;
    private final ModuleConstructor moduleConstructor;
    private List<Class<? extends ModuleBase>> moduleClassList;

    public ModuleRegistry(List<ModuleBase> moduleList, ModuleConstructor moduleConstructor) {

        this.moduleConstructor = moduleConstructor;

        this.moduleClassList = new ArrayList<>();
        this.moduleList = moduleList;
    }

    @SafeVarargs
    public final void registerModules(Class<? extends ModuleBase>... moduleClassArray) {

        this.moduleClassList.addAll(Arrays.asList(moduleClassArray));
    }

    public void initializeModules(String modId) {

        for (Class<? extends ModuleBase> moduleClass : this.moduleClassList) {
            ModuleBase module = this.moduleConstructor.constructModule(modId, moduleClass);

            if (module != null) {
                this.moduleList.add(module);
            }
        }

        // Don't really need to keep this around.
        this.moduleClassList = null;

        // Sort the module list by module priority.
        Collections.sort(this.moduleList);
    }

}
