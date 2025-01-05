package su.terrafirmagreg.framework.module.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

  /**
   * The ID of this module. Must be unique within its container.
   */
  String moduleID();

  /**
   * The ID of the container to associate this module with.
   */
  String containerID();

  /**
   * A human-readable name for this module.
   */
  String name();

  boolean enabled() default true;

  /**
   * Whether this module is the "core" module for its container. Each container must have exactly one core module, which will be loaded before all other modules
   * in the container.
   * <p>
   * Core modules should not have mod dependencies.
   */
  boolean coreModule() default false;

  String author() default "";

  String version() default "";

  /**
   * A description of this module in the module configuration file.
   */
  String[] description() default "";
}
