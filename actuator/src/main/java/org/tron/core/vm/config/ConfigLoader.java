package org.tron.core.vm.config;


import lombok.extern.slf4j.Slf4j;
import org.tron.common.utils.DBConfig;
import org.tron.core.exception.TypeMismatchNamingException;
import org.tron.core.store.DynamicPropertiesStore;
import org.tron.core.store.StoreFactory;

@Slf4j(topic = "VMConfigLoader")
public class ConfigLoader {

  //only for unit test
  public static boolean disable = false;

  public static void load() {
    StoreFactory storeFactory = StoreFactory.getInstance();
    if (!disable) {
      DynamicPropertiesStore ds = null;
      try {
        ds = storeFactory.getStore(DynamicPropertiesStore.class);
      } catch (TypeMismatchNamingException e) {
        logger.error("can not get DynamicPropertiesStore", e);
      }
      VMConfig.setVmTrace(DBConfig.isVmTrace());
      if (ds != null) {
        VMConfig.initVmHardFork(checkForEnergyLimit(ds));
        VMConfig.initAllowMultiSign(ds.getAllowMultiSign());
        VMConfig.initAllowTvmTransferTrc10(ds.getAllowTvmTransferTrc10());
        VMConfig.initAllowTvmConstantinople(ds.getAllowTvmConstantinople());
        VMConfig.initAllowTvmSolidity059(ds.getAllowTvmSolidity059());

      }
    }
  }

  public static void resetToDefault() {
    VMConfig.setVmTrace(false);
    VMConfig.initVmHardFork(false);
    VMConfig.initAllowMultiSign(0);
    VMConfig.initAllowTvmTransferTrc10(0);
    VMConfig.initAllowTvmConstantinople(0);
    VMConfig.initAllowTvmSolidity059(0);
  }

  private static boolean checkForEnergyLimit(DynamicPropertiesStore ds) {
    long blockNum = ds.getLatestBlockHeaderNumber();
    return blockNum >= DBConfig.getBlockNumForEneryLimit();
  }

}
