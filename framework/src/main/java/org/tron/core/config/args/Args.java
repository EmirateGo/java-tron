package org.tron.core.config.args;

import static java.lang.Math.max;
import static java.lang.System.exit;
import static org.tron.core.Constant.ADD_PRE_FIX_BYTE_MAINNET;
import static org.tron.core.config.Parameter.ChainConstant.BLOCK_PRODUCE_TIMEOUT_PERCENT;
import static org.tron.core.config.Parameter.ChainConstant.MAX_ACTIVE_WITNESS_NUM;

import com.beust.jcommander.JCommander;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import io.grpc.internal.GrpcUtil;
import io.grpc.netty.NettyServerBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.args.Account;
import org.tron.common.args.GenesisBlock;
import org.tron.common.args.Witness;
import org.tron.common.config.DbBackupConfig;
import org.tron.common.crypto.SignInterface;
import org.tron.common.logsfilter.EventPluginConfig;
import org.tron.common.logsfilter.FilterQuery;
import org.tron.common.logsfilter.TriggerConfig;
import org.tron.common.logsfilter.trigger.ContractEventTrigger;
import org.tron.common.logsfilter.trigger.ContractLogTrigger;
import org.tron.common.overlay.discover.node.Node;
import org.tron.common.parameter.CommonParameter;
import org.tron.common.parameter.RateLimiterInitialization;
import org.tron.common.setting.RocksDbSettings;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Commons;
import org.tron.common.utils.LocalWitnesses;
import org.tron.core.Constant;
import org.tron.core.Wallet;
import org.tron.core.config.Configuration;
import org.tron.core.config.Parameter.NetConstants;
import org.tron.core.config.Parameter.NodeConstant;
import org.tron.core.store.AccountStore;
import org.tron.keystore.CipherException;
import org.tron.keystore.Credentials;
import org.tron.keystore.WalletUtils;
import org.tron.program.Version;

@Slf4j(topic = "app")
@NoArgsConstructor
@Component
public class Args extends CommonParameter {

  @Getter
  @Setter
  private static LocalWitnesses localWitnesses = new LocalWitnesses();

  @Autowired(required = false)
  @Getter
  private static ConcurrentHashMap<Long, List<ContractLogTrigger>>
      solidityContractLogTriggerList =  new ConcurrentHashMap<>();

  @Autowired(required = false)
  @Getter
<<<<<<< HEAD
  @Parameter(description = "--seed-nodes")
  private List<String> seedNodes = new ArrayList<>();

  @Parameter(names = {"-p", "--private-key"}, description = "private-key")
  private String privateKey = "";

  @Parameter(names = {"--witness-address"}, description = "witness-address")
  private String witnessAddress = "";

  @Parameter(names = {"--password"}, description = "password")
  private String password;

  @Parameter(names = {"--storage-db-directory"}, description = "Storage db directory")
  private String storageDbDirectory = "";

  @Parameter(names = {"--storage-db-version"}, description = "Storage db version.(1 or 2)")
  private String storageDbVersion = "";

  @Parameter(names = {
      "--storage-db-engine"}, description = "Storage db engine.(leveldb or rocksdb)")
  private String storageDbEngine = "";

  @Parameter(names = {
      "--storage-db-synchronous"}, description = "Storage db is synchronous or not.(true or false)")
  private String storageDbSynchronous = "";

  @Parameter(names = {
      "--contract-parse-enable"}, description = "enable contract parses in java-tron or not.(true or false)")
  private String contractParseEnable = "";

  @Parameter(names = {"--storage-index-directory"}, description = "Storage index directory")
  private String storageIndexDirectory = "";

  @Parameter(names = {"--storage-index-switch"}, description = "Storage index switch.(on or off)")
  private String storageIndexSwitch = "";

  @Parameter(names = {
      "--storage-transactionHistory-switch"}, description = "Storage transaction history switch.(on or off)")
  private String storageTransactionHistoreSwitch = "";

  @Getter
  @Parameter(names = {"--fast-forward"})
  private boolean fastForward = false;

  @Getter
  private Storage storage;

  @Getter
  private Overlay overlay;

  @Getter
  private SeedNode seedNode;

  @Getter
  private GenesisBlock genesisBlock;

  @Getter
  @Setter
  private String chainId;

  @Getter
  @Setter
  private LocalWitnesses localWitnesses = new LocalWitnesses();

  @Getter
  @Setter
  private boolean needSyncCheck;

  @Getter
  @Setter
  private boolean nodeDiscoveryEnable;

  @Getter
  @Setter
  private boolean nodeDiscoveryPersist;

  @Getter
  @Setter
  private int nodeConnectionTimeout;

  @Getter
  @Setter
  private List<Node> activeNodes;

  @Getter
  @Setter
  private List<Node> passiveNodes;

  @Getter
  @Setter
  private List<Node> fastForwardNodes;

  @Getter
  @Setter
  private int nodeChannelReadTimeout;

  @Getter
  @Setter
  private int nodeMaxActiveNodes;

  @Getter
  @Setter
  private int nodeMaxActiveNodesWithSameIp;

  @Getter
  @Setter
  private int minParticipationRate;

  @Getter
  @Setter
  private int nodeListenPort;

  @Getter
  @Setter
  private String nodeDiscoveryBindIp;

  @Getter
  @Setter
  private String nodeExternalIp;

  @Getter
  @Setter
  private boolean nodeDiscoveryPublicHomeNode;

  @Getter
  @Setter
  private long nodeP2pPingInterval;

  @Getter
  @Setter
  @Parameter(names = {"--save-internaltx"})
  private boolean saveInternalTx;

  @Getter
  @Setter
  private int nodeP2pVersion;

  @Getter
  @Setter
  private String p2pNodeId;

  //If you are running a solidity node for java tron, this flag is set to true
  @Getter
  @Setter
  private boolean solidityNode = false;

  @Getter
  @Setter
  private int rpcPort;

  @Getter
  @Setter
  private int rpcOnSolidityPort;

  @Getter
  @Setter
  private int fullNodeHttpPort;

  @Getter
  @Setter
  private int solidityHttpPort;

  @Getter
  @Setter
  @Parameter(names = {"--rpc-thread"}, description = "Num of gRPC thread")
  private int rpcThreadNum;

  @Getter
  @Setter
  @Parameter(names = {"--solidity-thread"}, description = "Num of solidity thread")
  private int solidityThreads;

  @Getter
  @Setter
  private int maxConcurrentCallsPerConnection;

  @Getter
  @Setter
  private int flowControlWindow;

  @Getter
  @Setter
  private long maxConnectionIdleInMillis;

  @Getter
  @Setter
  private int blockProducedTimeOut;

  @Getter
  @Setter
  private long netMaxTrxPerSecond;

  @Getter
  @Setter
  private long maxConnectionAgeInMillis;

  @Getter
  @Setter
  private int maxMessageSize;

  @Getter
  @Setter
  private int maxHeaderListSize;

  @Getter
  @Setter
  @Parameter(names = {"--validate-sign-thread"}, description = "Num of validate thread")
  private int validateSignThreadNum;

  @Getter
  @Setter
  private long maintenanceTimeInterval; // (ms)

  @Getter
  @Setter
  private long proposalExpireTime; // (ms)

  @Getter
  @Setter
  private int checkFrozenTime; // for test only

  @Getter
  @Setter
  private long allowCreationOfContracts; //committee parameter

  @Getter
  @Setter
  private long allowAdaptiveEnergy; //committee parameter

  @Getter
  @Setter
  private long allowDelegateResource; //committee parameter

  @Getter
  @Setter
  private long allowSameTokenName; //committee parameter

  @Getter
  @Setter
  private long allowTvmTransferTrc10; //committee parameter

  @Getter
  @Setter
  private long allowTvmConstantinople; //committee parameter

  @Getter
  @Setter
  private long allowTvmSolidity059; //committee parameter

  @Getter
  @Setter
  private long forbidTransferToContract; //committee parameter

  @Getter
  @Setter
  private int tcpNettyWorkThreadNum;

  @Getter
  @Setter
  private int udpNettyWorkThreadNum;

  @Getter
  @Setter
  @Parameter(names = {"--trust-node"}, description = "Trust node addr")
  private String trustNodeAddr;

  @Getter
  @Setter
  private boolean walletExtensionApi;

  @Getter
  @Setter
  private int backupPriority;

  @Getter
  @Setter
  private int backupPort;

  @Getter
  @Setter
  private List<String> backupMembers;

  @Getter
  @Setter
  private double connectFactor;

  @Getter
  @Setter
  private double activeConnectFactor;

  @Getter
  @Setter
  private double disconnectNumberFactor;

  @Getter
  @Setter
  private double maxConnectNumberFactor;

  @Getter
  @Setter
  private long receiveTcpMinDataLength;

  @Getter
  @Setter
  private boolean isOpenFullTcpDisconnect;

  @Getter
  @Setter
  private int allowMultiSign;

  @Getter
  @Setter
  private boolean vmTrace;

  @Getter
  @Setter
  private boolean needToUpdateAsset;

  @Getter
  @Setter
  private String trxReferenceBlock;

  @Getter
  @Setter
  private int minEffectiveConnection;

//  @Getter
//  @Setter
//  private long allowShieldedTransaction; //committee parameter

  // full node used this parameter to close shielded transaction
  @Getter
  @Setter
  private boolean fullNodeAllowShieldedTransactionArgs;

  @Getter/**/
  @Setter
  private long allowShieldedTRC20Transaction;

  @Getter
  @Setter
  private long blockNumForEneryLimit;

  @Getter
  @Setter
  @Parameter(names = {"--es"})
  private boolean eventSubscribe = false;

  @Getter
  private EventPluginConfig eventPluginConfig;

  @Getter
  private FilterQuery eventFilter;

  @Getter
  @Setter
  private String cryptoEngine = Constant.ECKey_ENGINE;

  @Getter
  @Setter
  private long trxExpirationTimeInMilliseconds; // (ms)

  @Getter
  private DbBackupConfig dbBackupConfig;

  @Getter
  private RocksDbSettings rocksDBCustomSettings;

  @Parameter(names = {"-v", "--version"}, description = "output code version", help = true)
  private boolean version;


  @Getter
  @Setter
  private String zenTokenId;

  @Getter
  @Setter
  private long allowProtoFilterNum;

  @Getter
  @Setter
  private long allowAccountStateRoot;

  @Getter
  @Setter
  private int validContractProtoThreadNum;

  @Getter
  @Setter
  private int shieldedTransInPendingMaxCounts;

  @Getter
  @Setter
  private RateLimiterInitialization rateLimiterInitialization;

  @Getter
  @Setter
  private long changedDelegation;

  @Getter
  @Setter
  private Set<String> actuatorSet;

  @Getter
  @Setter
  public boolean fullNodeHttpEnable = true;

  @Getter
  @Setter
  public boolean solidityNodeHttpEnable = true;

  @Getter
  @Setter
  public boolean isEckey=true;
=======
  private static ConcurrentHashMap<Long, List<ContractEventTrigger>>
      solidityContractEventTriggerList =  new ConcurrentHashMap<>();
>>>>>>> develop

  public static void clearParam() {
    PARAMETER.outputDirectory = "output-directory";
    PARAMETER.help = false;
    PARAMETER.witness = false;
    PARAMETER.seedNodes = new ArrayList<>();
    PARAMETER.privateKey = "";
    PARAMETER.witnessAddress = "";
    PARAMETER.storageDbDirectory = "";
    PARAMETER.storageIndexDirectory = "";
    PARAMETER.storageIndexSwitch = "";

    // FIXME: PARAMETER.storage maybe null ?
    if (PARAMETER.storage != null) {
      // WARNING: WILL DELETE DB STORAGE PATHS
      PARAMETER.storage.deleteAllStoragePaths();
      PARAMETER.storage = null;
    }

<<<<<<< HEAD
    INSTANCE.overlay = null;
    INSTANCE.seedNode = null;
    INSTANCE.genesisBlock = null;
    INSTANCE.chainId = null;
    INSTANCE.localWitnesses = null;
    INSTANCE.needSyncCheck = false;
    INSTANCE.nodeDiscoveryEnable = false;
    INSTANCE.nodeDiscoveryPersist = false;
    INSTANCE.nodeConnectionTimeout = 0;
    INSTANCE.activeNodes = Collections.emptyList();
    INSTANCE.passiveNodes = Collections.emptyList();
    INSTANCE.fastForwardNodes = Collections.emptyList();
    INSTANCE.nodeChannelReadTimeout = 0;
    INSTANCE.nodeMaxActiveNodes = 30;
    INSTANCE.nodeMaxActiveNodesWithSameIp = 2;
    INSTANCE.minParticipationRate = 0;
    INSTANCE.nodeListenPort = 0;
    INSTANCE.nodeDiscoveryBindIp = "";
    INSTANCE.nodeExternalIp = "";
    INSTANCE.nodeDiscoveryPublicHomeNode = false;
    INSTANCE.nodeP2pPingInterval = 0L;
    INSTANCE.nodeP2pVersion = 0;
    INSTANCE.rpcPort = 0;
    INSTANCE.rpcOnSolidityPort = 0;
    INSTANCE.fullNodeHttpPort = 0;
    INSTANCE.solidityHttpPort = 0;
    INSTANCE.maintenanceTimeInterval = 0;
    INSTANCE.proposalExpireTime = 0;
    INSTANCE.checkFrozenTime = 1;
    INSTANCE.allowCreationOfContracts = 0;
    INSTANCE.allowAdaptiveEnergy = 0;
    INSTANCE.allowTvmTransferTrc10 = 0;
    INSTANCE.allowTvmConstantinople = 0;
    INSTANCE.allowDelegateResource = 0;
    INSTANCE.allowSameTokenName = 0;
    INSTANCE.allowTvmSolidity059 = 0;
    INSTANCE.forbidTransferToContract = 0;
    INSTANCE.tcpNettyWorkThreadNum = 0;
    INSTANCE.udpNettyWorkThreadNum = 0;
    INSTANCE.p2pNodeId = "";
    INSTANCE.solidityNode = false;
    INSTANCE.trustNodeAddr = "";
    INSTANCE.walletExtensionApi = false;
    INSTANCE.connectFactor = 0.3;
    INSTANCE.activeConnectFactor = 0.1;
    INSTANCE.disconnectNumberFactor = 0.4;
    INSTANCE.maxConnectNumberFactor = 0.8;
    INSTANCE.receiveTcpMinDataLength = 2048;
    INSTANCE.isOpenFullTcpDisconnect = false;
    INSTANCE.supportConstant = false;
    INSTANCE.debug = false;
    INSTANCE.minTimeRatio = 0.0;
    INSTANCE.maxTimeRatio = 5.0;
    INSTANCE.longRunningTime = 10;
//    INSTANCE.allowShieldedTransaction = 0;
    INSTANCE.allowShieldedTRC20Transaction = 0;
    INSTANCE.maxHttpConnectNumber = 50;
    INSTANCE.allowMultiSign = 0;
    INSTANCE.trxExpirationTimeInMilliseconds = 0;
    INSTANCE.fullNodeAllowShieldedTransactionArgs = true;
    INSTANCE.zenTokenId = "000000";
    INSTANCE.allowProtoFilterNum = 0;
    INSTANCE.allowAccountStateRoot = 0;
    INSTANCE.validContractProtoThreadNum = 1;
    INSTANCE.shieldedTransInPendingMaxCounts = 10;
    INSTANCE.changedDelegation = 0;
    INSTANCE.fullNodeHttpEnable = true;
    INSTANCE.solidityNodeHttpEnable = true;
    INSTANCE.isEckey = true;
=======
    PARAMETER.overlay = null;
    PARAMETER.seedNode = null;
    PARAMETER.genesisBlock = null;
    PARAMETER.chainId = null;
    localWitnesses = null;
    PARAMETER.needSyncCheck = false;
    PARAMETER.nodeDiscoveryEnable = false;
    PARAMETER.nodeDiscoveryPersist = false;
    PARAMETER.nodeConnectionTimeout = 0;
    PARAMETER.activeNodes = Collections.emptyList();
    PARAMETER.passiveNodes = Collections.emptyList();
    PARAMETER.fastForwardNodes = Collections.emptyList();
    PARAMETER.nodeChannelReadTimeout = 0;
    PARAMETER.nodeMaxActiveNodes = 30;
    PARAMETER.nodeMaxActiveNodesWithSameIp = 2;
    PARAMETER.minParticipationRate = 0;
    PARAMETER.nodeListenPort = 0;
    PARAMETER.nodeDiscoveryBindIp = "";
    PARAMETER.nodeExternalIp = "";
    PARAMETER.nodeDiscoveryPublicHomeNode = false;
    PARAMETER.nodeP2pPingInterval = 0L;
    PARAMETER.nodeP2pVersion = 0;
    PARAMETER.rpcPort = 0;
    PARAMETER.rpcOnSolidityPort = 0;
    PARAMETER.rpcOnPBFTPort = 0;
    PARAMETER.fullNodeHttpPort = 0;
    PARAMETER.solidityHttpPort = 0;
    PARAMETER.pBFTHttpPort = 0;
    PARAMETER.maintenanceTimeInterval = 0;
    PARAMETER.proposalExpireTime = 0;
    PARAMETER.checkFrozenTime = 1;
    PARAMETER.allowCreationOfContracts = 0;
    PARAMETER.allowAdaptiveEnergy = 0;
    PARAMETER.allowTvmTransferTrc10 = 0;
    PARAMETER.allowTvmConstantinople = 0;
    PARAMETER.allowDelegateResource = 0;
    PARAMETER.allowSameTokenName = 0;
    PARAMETER.allowTvmSolidity059 = 0;
    PARAMETER.forbidTransferToContract = 0;
    PARAMETER.tcpNettyWorkThreadNum = 0;
    PARAMETER.udpNettyWorkThreadNum = 0;
    PARAMETER.p2pNodeId = "";
    PARAMETER.solidityNode = false;
    PARAMETER.trustNodeAddr = "";
    PARAMETER.walletExtensionApi = false;
    PARAMETER.connectFactor = 0.3;
    PARAMETER.activeConnectFactor = 0.1;
    PARAMETER.disconnectNumberFactor = 0.4;
    PARAMETER.maxConnectNumberFactor = 0.8;
    PARAMETER.receiveTcpMinDataLength = 2048;
    PARAMETER.isOpenFullTcpDisconnect = false;
    PARAMETER.supportConstant = false;
    PARAMETER.debug = false;
    PARAMETER.minTimeRatio = 0.0;
    PARAMETER.maxTimeRatio = 5.0;
    PARAMETER.longRunningTime = 10;
    PARAMETER.allowShieldedTransaction = 0;
    PARAMETER.maxHttpConnectNumber = 50;
    PARAMETER.allowMultiSign = 0;
    PARAMETER.trxExpirationTimeInMilliseconds = 0;
    PARAMETER.fullNodeAllowShieldedTransactionArgs = true;
    PARAMETER.zenTokenId = "000000";
    PARAMETER.allowProtoFilterNum = 0;
    PARAMETER.allowAccountStateRoot = 0;
    PARAMETER.validContractProtoThreadNum = 1;
    PARAMETER.shieldedTransInPendingMaxCounts = 10;
    PARAMETER.changedDelegation = 0;
    PARAMETER.fullNodeHttpEnable = true;
    PARAMETER.solidityNodeHttpEnable = true;
    PARAMETER.nodeMetricsEnable = true;
    PARAMETER.agreeNodeCount = MAX_ACTIVE_WITNESS_NUM * 2 / 3 + 1;
    PARAMETER.allowPBFT = 0;
>>>>>>> develop
  }

  /**
   * set parameters.
   */
  public static void setParam(final String[] args, final String confFileName) {
    JCommander.newBuilder().addObject(PARAMETER).build().parse(args);
    if (PARAMETER.version) {
      JCommander.getConsole()
          .println(Version.getVersion()
              + "\n" + Version.versionName + "\n" + Version.versionCode);
      exit(0);
    }

    Config config = Configuration.getByFileName(PARAMETER.shellConfFileName, confFileName);

    if (config.hasPath(Constant.NET_TYPE)
        && Constant.TESTNET.equalsIgnoreCase(config.getString(Constant.NET_TYPE))) {
      Wallet.setAddressPreFixByte(Constant.ADD_PRE_FIX_BYTE_TESTNET);
      Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_TESTNET);
    } else {
      Wallet.setAddressPreFixByte(ADD_PRE_FIX_BYTE_MAINNET);
      Wallet.setAddressPreFixString(Constant.ADD_PRE_FIX_STRING_MAINNET);
    }

    PARAMETER.cryptoEngine = config.hasPath(Constant.CRYPTO_ENGINE) ? config
        .getString(Constant.CRYPTO_ENGINE) : Constant.ECKey_ENGINE;

    if (StringUtils.isNoneBlank(PARAMETER.privateKey)) {
      localWitnesses = (new LocalWitnesses(PARAMETER.privateKey));
      if (StringUtils.isNoneBlank(PARAMETER.witnessAddress)) {
        byte[] bytes = Commons.decodeFromBase58Check(PARAMETER.witnessAddress);
        if (bytes != null) {
          localWitnesses.setWitnessAccountAddress(bytes);
          logger.debug("Got localWitnessAccountAddress from cmd");
        } else {
          PARAMETER.witnessAddress = "";
          logger.warn(IGNORE_WRONG_WITNESS_ADDRESS_FORMAT);
        }
      }
      localWitnesses.initWitnessAccountAddress(PARAMETER.isECKeyCryptoEngine());
      logger.debug("Got privateKey from cmd");
<<<<<<< HEAD
    } else if (config.hasPath(Constant.LOCAL_WITENSS)) {
      INSTANCE.localWitnesses = new LocalWitnesses();
      List<String> localwitness = config.getStringList(Constant.LOCAL_WITENSS);
    //if (localwitness.size() > 1) {
    //  logger.warn("localwitness size must be one, get the first one");
    //  localwitness = localwitness.subList(0, 1);
    //}
      INSTANCE.localWitnesses.setPrivateKeys(localwitness);
=======
    } else if (config.hasPath(Constant.LOCAL_WITNESS)) {
      localWitnesses = new LocalWitnesses();
      List<String> localwitness = config.getStringList(Constant.LOCAL_WITNESS);
      if (localwitness.size() > 1) {
        logger.warn("localwitness size must be one, get the first one");
        localwitness = localwitness.subList(0, 1);
      }
      localWitnesses.setPrivateKeys(localwitness);
>>>>>>> develop

      if (config.hasPath(Constant.LOCAL_WITNESS_ACCOUNT_ADDRESS)) {
        byte[] bytes = Commons
            .decodeFromBase58Check(config.getString(Constant.LOCAL_WITNESS_ACCOUNT_ADDRESS));
        if (bytes != null) {
          localWitnesses.setWitnessAccountAddress(bytes);
          logger.debug("Got localWitnessAccountAddress from config.conf");
        } else {
          logger.warn(IGNORE_WRONG_WITNESS_ADDRESS_FORMAT);
        }
      }
      localWitnesses.initWitnessAccountAddress(PARAMETER.isECKeyCryptoEngine());
      logger.debug("Got privateKey from config.conf");
    } else if (config.hasPath(Constant.LOCAL_WITNESS_KEYSTORE)) {
      localWitnesses = new LocalWitnesses();
      List<String> privateKeys = new ArrayList<String>();
      if (PARAMETER.isWitness()) {
        List<String> localwitness = config.getStringList(Constant.LOCAL_WITNESS_KEYSTORE);
        if (localwitness.size() > 0) {
          String fileName = System.getProperty("user.dir") + "/" + localwitness.get(0);
          String password;
          if (StringUtils.isEmpty(PARAMETER.password)) {
            System.out.println("Please input your password.");
            password = WalletUtils.inputPassword();
          } else {
            password = PARAMETER.password;
            PARAMETER.password = null;
          }

          try {
            Credentials credentials = WalletUtils
                .loadCredentials(password, new File(fileName));
            SignInterface sign = credentials.getSignInterface();
            String prikey = ByteArray.toHexString(sign.getPrivateKey());
            privateKeys.add(prikey);
          } catch (IOException | CipherException e) {
            logger.error(e.getMessage());
            logger.error("Witness node start failed!");
            exit(-1);
          }
        }
      }
      localWitnesses.setPrivateKeys(privateKeys);

      if (config.hasPath(Constant.LOCAL_WITNESS_ACCOUNT_ADDRESS)) {
        byte[] bytes = Commons
            .decodeFromBase58Check(config.getString(Constant.LOCAL_WITNESS_ACCOUNT_ADDRESS));
        if (bytes != null) {
          localWitnesses.setWitnessAccountAddress(bytes);
          logger.debug("Got localWitnessAccountAddress from config.conf");
        } else {
          logger.warn(IGNORE_WRONG_WITNESS_ADDRESS_FORMAT);
        }
      }
      localWitnesses.initWitnessAccountAddress(PARAMETER.isECKeyCryptoEngine());
      logger.debug("Got privateKey from keystore");
    }

    if (PARAMETER.isWitness()
        && CollectionUtils.isEmpty(localWitnesses.getPrivateKeys())) {
      logger.warn("This is a witness node, but localWitnesses is null");
    }

    if (config.hasPath(Constant.VM_SUPPORT_CONSTANT)) {
      PARAMETER.supportConstant = config.getBoolean(Constant.VM_SUPPORT_CONSTANT);
    }

    if (config.hasPath(Constant.NODE_HTTP_FULLNODE_ENABLE)) {
      PARAMETER.fullNodeHttpEnable = config.getBoolean(Constant.NODE_HTTP_FULLNODE_ENABLE);
    }

    if (config.hasPath(Constant.NODE_HTTP_SOLIDITY_ENABLE)) {
      PARAMETER.solidityNodeHttpEnable = config.getBoolean(Constant.NODE_HTTP_SOLIDITY_ENABLE);
    }

    if (config.hasPath(Constant.NODE_HTTP_STATISTICS_SR_REWARD_SWITCH)) {
      PARAMETER.nodeHttpStatisticsSRRewardEnable = config
          .getBoolean(Constant.NODE_HTTP_STATISTICS_SR_REWARD_SWITCH);
    }

    if (config.hasPath(Constant.VM_MIN_TIME_RATIO)) {
      PARAMETER.minTimeRatio = config.getDouble(Constant.VM_MIN_TIME_RATIO);
    }

    if (config.hasPath(Constant.VM_MAX_TIME_RATIO)) {
      PARAMETER.maxTimeRatio = config.getDouble(Constant.VM_MAX_TIME_RATIO);
    }

    if (config.hasPath(Constant.VM_LONG_RUNNING_TIME)) {
      PARAMETER.longRunningTime = config.getInt(Constant.VM_LONG_RUNNING_TIME);
    }

    PARAMETER.storage = new Storage();
    PARAMETER.storage.setDbVersion(Optional.ofNullable(PARAMETER.storageDbVersion)
        .filter(StringUtils::isNotEmpty)
        .map(Integer::valueOf)
        .orElse(Storage.getDbVersionFromConfig(config)));

    PARAMETER.storage.setDbEngine(Optional.ofNullable(PARAMETER.storageDbEngine)
        .filter(StringUtils::isNotEmpty)
        .orElse(Storage.getDbEngineFromConfig(config)));

    if (Constant.ROCKSDB.equals(PARAMETER.storage.getDbEngine().toUpperCase())
        && PARAMETER.storage.getDbVersion() == 1) {
      throw new RuntimeException("db.version = 1 is not supported by ROCKSDB engine.");
    }

    PARAMETER.storage.setDbSync(Optional.ofNullable(PARAMETER.storageDbSynchronous)
        .filter(StringUtils::isNotEmpty)
        .map(Boolean::valueOf)
        .orElse(Storage.getDbVersionSyncFromConfig(config)));

    PARAMETER.storage.setContractParseSwitch(Optional.ofNullable(PARAMETER.contractParseEnable)
        .filter(StringUtils::isNotEmpty)
        .map(Boolean::valueOf)
        .orElse(Storage.getContractParseSwitchFromConfig(config)));

    PARAMETER.storage.setDbDirectory(Optional.ofNullable(PARAMETER.storageDbDirectory)
        .filter(StringUtils::isNotEmpty)
        .orElse(Storage.getDbDirectoryFromConfig(config)));

    PARAMETER.storage.setIndexDirectory(Optional.ofNullable(PARAMETER.storageIndexDirectory)
        .filter(StringUtils::isNotEmpty)
        .orElse(Storage.getIndexDirectoryFromConfig(config)));

    PARAMETER.storage.setIndexSwitch(Optional.ofNullable(PARAMETER.storageIndexSwitch)
        .filter(StringUtils::isNotEmpty)
        .orElse(Storage.getIndexSwitchFromConfig(config)));

    PARAMETER.storage
        .setTransactionHistorySwitch(
            Optional.ofNullable(PARAMETER.storageTransactionHistorySwitch)
                .filter(StringUtils::isNotEmpty)
                .orElse(Storage.getTransactionHistorySwitchFromConfig(config)));

    PARAMETER.storage.setPropertyMapFromConfig(config);

    PARAMETER.seedNode = new SeedNode();
    PARAMETER.seedNode.setIpList(Optional.ofNullable(PARAMETER.seedNodes)
        .filter(seedNode -> 0 != seedNode.size())
        .orElse(config.getStringList(Constant.SEED_NODE_IP_LIST)));

    if (config.hasPath(Constant.GENESIS_BLOCK)) {
      PARAMETER.genesisBlock = new GenesisBlock();

      PARAMETER.genesisBlock.setTimestamp(config.getString(Constant.GENESIS_BLOCK_TIMESTAMP));
      PARAMETER.genesisBlock.setParentHash(config.getString(Constant.GENESIS_BLOCK_PARENTHASH));

      if (config.hasPath(Constant.GENESIS_BLOCK_ASSETS)) {
        PARAMETER.genesisBlock.setAssets(getAccountsFromConfig(config));
        AccountStore.setAccount(config);
      }
      if (config.hasPath(Constant.GENESIS_BLOCK_WITNESSES)) {
        PARAMETER.genesisBlock.setWitnesses(getWitnessesFromConfig(config));
      }
    } else {
      PARAMETER.genesisBlock = GenesisBlock.getDefault();
    }

    PARAMETER.needSyncCheck =
        config.hasPath(Constant.BLOCK_NEED_SYNC_CHECK)
            && config.getBoolean(Constant.BLOCK_NEED_SYNC_CHECK);

    PARAMETER.nodeDiscoveryEnable =
        config.hasPath(Constant.NODE_DISCOVERY_ENABLE)
            && config.getBoolean(Constant.NODE_DISCOVERY_ENABLE);

    PARAMETER.nodeDiscoveryPersist =
        config.hasPath(Constant.NODE_DISCOVERY_PERSIST)
            && config.getBoolean(Constant.NODE_DISCOVERY_PERSIST);

    PARAMETER.nodeConnectionTimeout =
        config.hasPath(Constant.NODE_CONNECTION_TIMEOUT)
            ? config.getInt(Constant.NODE_CONNECTION_TIMEOUT) * 1000
            : 0;

    PARAMETER.nodeChannelReadTimeout =
        config.hasPath(Constant.NODE_CHANNEL_READ_TIMEOUT)
            ? config.getInt(Constant.NODE_CHANNEL_READ_TIMEOUT)
            : 0;

    PARAMETER.nodeMaxActiveNodes =
        config.hasPath(Constant.NODE_MAX_ACTIVE_NODES)
            ? config.getInt(Constant.NODE_MAX_ACTIVE_NODES) : 30;

    PARAMETER.nodeMaxActiveNodesWithSameIp =
        config.hasPath(Constant.NODE_MAX_ACTIVE_NODES_WITH_SAMEIP) ? config
            .getInt(Constant.NODE_MAX_ACTIVE_NODES_WITH_SAMEIP) : 2;

    PARAMETER.minParticipationRate =
        config.hasPath(Constant.NODE_MIN_PARTICIPATION_RATE)
            ? config.getInt(Constant.NODE_MIN_PARTICIPATION_RATE)
            : 0;

    PARAMETER.nodeListenPort =
        config.hasPath(Constant.NODE_LISTEN_PORT)
            ? config.getInt(Constant.NODE_LISTEN_PORT) : 0;

    bindIp(config);
    externalIp(config);

    PARAMETER.nodeDiscoveryPublicHomeNode =
        config.hasPath(Constant.NODE_DISCOVERY_PUBLIC_HOME_NODE) && config
            .getBoolean(Constant.NODE_DISCOVERY_PUBLIC_HOME_NODE);

    PARAMETER.nodeP2pPingInterval =
        config.hasPath(Constant.NODE_P2P_PING_INTERVAL)
            ? config.getLong(Constant.NODE_P2P_PING_INTERVAL) : 0;

    PARAMETER.nodeP2pVersion =
        config.hasPath(Constant.NODE_P2P_VERSION)
            ? config.getInt(Constant.NODE_P2P_VERSION) : 0;

    PARAMETER.rpcPort =
        config.hasPath(Constant.NODE_RPC_PORT)
            ? config.getInt(Constant.NODE_RPC_PORT) : 50051;

    PARAMETER.rpcOnSolidityPort =
        config.hasPath(Constant.NODE_RPC_SOLIDITY_PORT)
            ? config.getInt(Constant.NODE_RPC_SOLIDITY_PORT) : 50061;

    PARAMETER.rpcOnPBFTPort =
        config.hasPath(Constant.NODE_RPC_PBFT_PORT)
            ? config.getInt(Constant.NODE_RPC_PBFT_PORT) : 50071;

    PARAMETER.fullNodeHttpPort =
        config.hasPath(Constant.NODE_HTTP_FULLNODE_PORT)
            ? config.getInt(Constant.NODE_HTTP_FULLNODE_PORT) : 8090;

    PARAMETER.solidityHttpPort =
        config.hasPath(Constant.NODE_HTTP_SOLIDITY_PORT)
            ? config.getInt(Constant.NODE_HTTP_SOLIDITY_PORT) : 8091;

    PARAMETER.pBFTHttpPort =
        config.hasPath(Constant.NODE_HTTP_PBFT_PORT)
            ? config.getInt(Constant.NODE_HTTP_PBFT_PORT) : 8092;

    PARAMETER.rpcThreadNum =
        config.hasPath(Constant.NODE_RPC_THREAD) ? config.getInt(Constant.NODE_RPC_THREAD)
            : (Runtime.getRuntime().availableProcessors() + 1) / 2;

    PARAMETER.solidityThreads =
        config.hasPath(Constant.NODE_SOLIDITY_THREADS)
            ? config.getInt(Constant.NODE_SOLIDITY_THREADS)
            : Runtime.getRuntime().availableProcessors();

    PARAMETER.maxConcurrentCallsPerConnection =
        config.hasPath(Constant.NODE_RPC_MAX_CONCURRENT_CALLS_PER_CONNECTION)
            ? config.getInt(Constant.NODE_RPC_MAX_CONCURRENT_CALLS_PER_CONNECTION)
            : Integer.MAX_VALUE;

    PARAMETER.flowControlWindow = config.hasPath(Constant.NODE_RPC_FLOW_CONTROL_WINDOW)
        ? config.getInt(Constant.NODE_RPC_FLOW_CONTROL_WINDOW)
        : NettyServerBuilder.DEFAULT_FLOW_CONTROL_WINDOW;

    PARAMETER.maxConnectionIdleInMillis =
        config.hasPath(Constant.NODE_RPC_MAX_CONNECTION_IDLE_IN_MILLIS)
            ? config.getLong(Constant.NODE_RPC_MAX_CONNECTION_IDLE_IN_MILLIS)
            : Long.MAX_VALUE;

    PARAMETER.blockProducedTimeOut = config.hasPath(Constant.NODE_PRODUCED_TIMEOUT)
        ? config.getInt(Constant.NODE_PRODUCED_TIMEOUT) : BLOCK_PRODUCE_TIMEOUT_PERCENT;

    PARAMETER.maxHttpConnectNumber = config.hasPath(Constant.NODE_MAX_HTTP_CONNECT_NUMBER)
        ? config.getInt(Constant.NODE_MAX_HTTP_CONNECT_NUMBER)
        : NodeConstant.MAX_HTTP_CONNECT_NUMBER;

    if (PARAMETER.blockProducedTimeOut < 30) {
      PARAMETER.blockProducedTimeOut = 30;
    }
    if (PARAMETER.blockProducedTimeOut > 100) {
      PARAMETER.blockProducedTimeOut = 100;
    }

    PARAMETER.netMaxTrxPerSecond = config.hasPath(Constant.NODE_NET_MAX_TRX_PER_SECOND)
        ? config.getInt(Constant.NODE_NET_MAX_TRX_PER_SECOND)
        : NetConstants.NET_MAX_TRX_PER_SECOND;

    PARAMETER.maxConnectionAgeInMillis =
        config.hasPath(Constant.NODE_RPC_MAX_CONNECTION_AGE_IN_MILLIS)
            ? config.getLong(Constant.NODE_RPC_MAX_CONNECTION_AGE_IN_MILLIS)
            : Long.MAX_VALUE;

    PARAMETER.maxMessageSize = config.hasPath(Constant.NODE_RPC_MAX_MESSAGE_SIZE)
        ? config.getInt(Constant.NODE_RPC_MAX_MESSAGE_SIZE) : GrpcUtil.DEFAULT_MAX_MESSAGE_SIZE;

    PARAMETER.maxHeaderListSize = config.hasPath(Constant.NODE_RPC_MAX_HEADER_LIST_SIZE)
        ? config.getInt(Constant.NODE_RPC_MAX_HEADER_LIST_SIZE)
        : GrpcUtil.DEFAULT_MAX_HEADER_LIST_SIZE;

    PARAMETER.maintenanceTimeInterval =
        config.hasPath(Constant.BLOCK_MAINTENANCE_TIME_INTERVAL) ? config
            .getInt(Constant.BLOCK_MAINTENANCE_TIME_INTERVAL) : 21600000L;

    PARAMETER.proposalExpireTime =
        config.hasPath(Constant.BLOCK_PROPOSAL_EXPIRE_TIME) ? config
            .getInt(Constant.BLOCK_PROPOSAL_EXPIRE_TIME) : 259200000L;

    PARAMETER.checkFrozenTime =
        config.hasPath(Constant.BLOCK_CHECK_FROZEN_TIME) ? config
            .getInt(Constant.BLOCK_CHECK_FROZEN_TIME) : 1;

    PARAMETER.allowCreationOfContracts =
        config.hasPath(Constant.COMMITTEE_ALLOW_CREATION_OF_CONTRACTS) ? config
            .getInt(Constant.COMMITTEE_ALLOW_CREATION_OF_CONTRACTS) : 0;

    PARAMETER.allowMultiSign =
        config.hasPath(Constant.COMMITTEE_ALLOW_MULTI_SIGN) ? config
            .getInt(Constant.COMMITTEE_ALLOW_MULTI_SIGN) : 0;

    PARAMETER.allowAdaptiveEnergy =
        config.hasPath(Constant.COMMITTEE_ALLOW_ADAPTIVE_ENERGY) ? config
            .getInt(Constant.COMMITTEE_ALLOW_ADAPTIVE_ENERGY) : 0;

    PARAMETER.allowDelegateResource =
        config.hasPath(Constant.COMMITTEE_ALLOW_DELEGATE_RESOURCE) ? config
            .getInt(Constant.COMMITTEE_ALLOW_DELEGATE_RESOURCE) : 0;

    PARAMETER.allowSameTokenName =
        config.hasPath(Constant.COMMITTEE_ALLOW_SAME_TOKEN_NAME) ? config
            .getInt(Constant.COMMITTEE_ALLOW_SAME_TOKEN_NAME) : 0;

    PARAMETER.allowTvmTransferTrc10 =
        config.hasPath(Constant.COMMITTEE_ALLOW_TVM_TRANSFER_TRC10) ? config
            .getInt(Constant.COMMITTEE_ALLOW_TVM_TRANSFER_TRC10) : 0;

    PARAMETER.allowTvmConstantinople =
        config.hasPath(Constant.COMMITTEE_ALLOW_TVM_CONSTANTINOPLE) ? config
            .getInt(Constant.COMMITTEE_ALLOW_TVM_CONSTANTINOPLE) : 0;

    PARAMETER.allowTvmSolidity059 =
        config.hasPath(Constant.COMMITTEE_ALLOW_TVM_SOLIDITY059) ? config
            .getInt(Constant.COMMITTEE_ALLOW_TVM_SOLIDITY059) : 0;

    PARAMETER.forbidTransferToContract =
        config.hasPath(Constant.COMMITTEE_FORBID_TRANSFER_TO_CONTRACT) ? config
            .getInt(Constant.COMMITTEE_FORBID_TRANSFER_TO_CONTRACT) : 0;

    PARAMETER.tcpNettyWorkThreadNum = config.hasPath(Constant.NODE_TCP_NETTY_WORK_THREAD_NUM)
        ? config.getInt(Constant.NODE_TCP_NETTY_WORK_THREAD_NUM) : 0;

    PARAMETER.udpNettyWorkThreadNum = config.hasPath(Constant.NODE_UDP_NETTY_WORK_THREAD_NUM)
        ? config.getInt(Constant.NODE_UDP_NETTY_WORK_THREAD_NUM) : 1;

    if (StringUtils.isEmpty(PARAMETER.trustNodeAddr)) {
      PARAMETER.trustNodeAddr =
          config.hasPath(Constant.NODE_TRUST_NODE)
              ? config.getString(Constant.NODE_TRUST_NODE) : null;
    }

    PARAMETER.validateSignThreadNum =
        config.hasPath(Constant.NODE_VALIDATE_SIGN_THREAD_NUM) ? config
            .getInt(Constant.NODE_VALIDATE_SIGN_THREAD_NUM)
            : (Runtime.getRuntime().availableProcessors() + 1) / 2;

    PARAMETER.walletExtensionApi =
        config.hasPath(Constant.NODE_WALLET_EXTENSION_API)
            && config.getBoolean(Constant.NODE_WALLET_EXTENSION_API);

    PARAMETER.connectFactor =
        config.hasPath(Constant.NODE_CONNECT_FACTOR)
            ? config.getDouble(Constant.NODE_CONNECT_FACTOR) : 0.3;

    PARAMETER.activeConnectFactor = config.hasPath(Constant.NODE_ACTIVE_CONNECT_FACTOR)
        ? config.getDouble(Constant.NODE_ACTIVE_CONNECT_FACTOR) : 0.1;

    PARAMETER.disconnectNumberFactor = config.hasPath(Constant.NODE_DISCONNECT_NUMBER_FACTOR)
        ? config.getDouble(Constant.NODE_DISCONNECT_NUMBER_FACTOR) : 0.4;
    PARAMETER.maxConnectNumberFactor = config.hasPath(Constant.NODE_MAX_CONNECT_NUMBER_FACTOR)
        ? config.getDouble(Constant.NODE_MAX_CONNECT_NUMBER_FACTOR) : 0.8;
    PARAMETER.receiveTcpMinDataLength = config.hasPath(Constant.NODE_RECEIVE_TCP_MIN_DATA_LENGTH)
        ? config.getLong(Constant.NODE_RECEIVE_TCP_MIN_DATA_LENGTH) : 2048;
    PARAMETER.isOpenFullTcpDisconnect = config.hasPath(Constant.NODE_IS_OPEN_FULL_TCP_DISCONNECT)
        && config.getBoolean(Constant.NODE_IS_OPEN_FULL_TCP_DISCONNECT);
    PARAMETER.maxTransactionPendingSize = config.hasPath(Constant.NODE_MAX_TRANSACTION_PENDING_SIZE)
        ? config.getInt(Constant.NODE_MAX_TRANSACTION_PENDING_SIZE) : 2000;

    PARAMETER.pendingTransactionTimeout = config.hasPath(Constant.NODE_PENDING_TRANSACTION_TIMEOUT)
        ? config.getLong(Constant.NODE_PENDING_TRANSACTION_TIMEOUT) : 60_000;

    PARAMETER.needToUpdateAsset =
        config.hasPath(Constant.STORAGE_NEEDTO_UPDATE_ASSET) ? config
            .getBoolean(Constant.STORAGE_NEEDTO_UPDATE_ASSET)
            : true;
    PARAMETER.trxReferenceBlock = config.hasPath(Constant.TRX_REFERENCE_BLOCK)
        ? config.getString(Constant.TRX_REFERENCE_BLOCK) : "head";

    PARAMETER.trxExpirationTimeInMilliseconds =
        config.hasPath(Constant.TRX_EXPIRATION_TIME_IN_MILLIS_SECONDS)
            && config.getLong(Constant.TRX_EXPIRATION_TIME_IN_MILLIS_SECONDS) > 0
            ? config.getLong(Constant.TRX_EXPIRATION_TIME_IN_MILLIS_SECONDS)
            : Constant.TRANSACTION_DEFAULT_EXPIRATION_TIME;

    PARAMETER.minEffectiveConnection = config.hasPath(Constant.NODE_RPC_MIN_EFFECTIVE_CONNECTION)
        ? config.getInt(Constant.NODE_RPC_MIN_EFFECTIVE_CONNECTION) : 1;

    PARAMETER.blockNumForEnergyLimit = config.hasPath(Constant.ENERGY_LIMIT_BLOCK_NUM)
        ? config.getInt(Constant.ENERGY_LIMIT_BLOCK_NUM) : 4727890L;

    PARAMETER.vmTrace =
        config.hasPath(Constant.VM_TRACE) && config.getBoolean(Constant.VM_TRACE);

    PARAMETER.saveInternalTx =
        config.hasPath(Constant.VM_SAVE_INTERNAL_TX)
            && config.getBoolean(Constant.VM_SAVE_INTERNAL_TX);

    PARAMETER.allowShieldedTransaction =
        config.hasPath(Constant.COMMITTEE_ALLOW_SHIELDED_TRANSACTION) ? config
            .getInt(Constant.COMMITTEE_ALLOW_SHIELDED_TRANSACTION) : 0;

<<<<<<< HEAD
    INSTANCE.allowShieldedTRC20Transaction =
        config.hasPath(Constant.COMMITTEE_ALLOW_SHIELDED_TRC20_TRANSACTION) ? config
            .getInt(Constant.COMMITTEE_ALLOW_SHIELDED_TRC20_TRANSACTION) : 0;

    INSTANCE.eventPluginConfig =
        config.hasPath(Constant.EVENT_SUBSCRIBE) ?
            getEventPluginConfig(config) : null;
=======
    PARAMETER.eventPluginConfig =
        config.hasPath(Constant.EVENT_SUBSCRIBE)
            ? getEventPluginConfig(config) : null;
>>>>>>> develop

    PARAMETER.eventFilter =
        config.hasPath(Constant.EVENT_SUBSCRIBE_FILTER) ? getEventFilter(config) : null;

    PARAMETER.fullNodeAllowShieldedTransactionArgs =
        !config.hasPath(Constant.NODE_FULLNODE_ALLOW_SHIELDED_TRANSACTION)
            || config.getBoolean(Constant.NODE_FULLNODE_ALLOW_SHIELDED_TRANSACTION);

    PARAMETER.zenTokenId = config.hasPath(Constant.NODE_ZEN_TOKENID)
        ? config.getString(Constant.NODE_ZEN_TOKENID) : "000000";

    PARAMETER.allowProtoFilterNum =
        config.hasPath(Constant.COMMITTEE_ALLOW_PROTO_FILTER_NUM) ? config
            .getInt(Constant.COMMITTEE_ALLOW_PROTO_FILTER_NUM) : 0;

    PARAMETER.allowAccountStateRoot =
        config.hasPath(Constant.COMMITTEE_ALLOW_ACCOUNT_STATE_ROOT) ? config
            .getInt(Constant.COMMITTEE_ALLOW_ACCOUNT_STATE_ROOT) : 0;

    PARAMETER.validContractProtoThreadNum =
        config.hasPath(Constant.NODE_VALID_CONTRACT_PROTO_THREADS) ? config
            .getInt(Constant.NODE_VALID_CONTRACT_PROTO_THREADS)
            : Runtime.getRuntime().availableProcessors();

    PARAMETER.activeNodes = getNodes(config, Constant.NODE_ACTIVE);

    PARAMETER.passiveNodes = getNodes(config, Constant.NODE_PASSIVE);

    PARAMETER.fastForwardNodes = getNodes(config, Constant.NODE_FAST_FORWARD);
    PARAMETER.shieldedTransInPendingMaxCounts =
        config.hasPath(Constant.NODE_SHIELDED_TRANS_IN_PENDING_MAX_COUNTS) ? config
            .getInt(Constant.NODE_SHIELDED_TRANS_IN_PENDING_MAX_COUNTS) : 10;

    if (PARAMETER.isWitness()) {
      PARAMETER.fullNodeAllowShieldedTransactionArgs = true;
    }

    PARAMETER.rateLimiterInitialization =
        config.hasPath(Constant.RATE_LIMITER) ? getRateLimiterFromConfig(config)
            : new RateLimiterInitialization();

    PARAMETER.changedDelegation =
        config.hasPath(Constant.COMMITTEE_CHANGED_DELEGATION) ? config
            .getInt(Constant.COMMITTEE_CHANGED_DELEGATION) : 0;

    PARAMETER.allowPBFT =
        config.hasPath(Constant.COMMITTEE_ALLOW_PBFT) ? config
            .getLong(Constant.COMMITTEE_ALLOW_PBFT) : 0;

    PARAMETER.agreeNodeCount = config.hasPath("node.agreeNodeCount") ? config
        .getInt("node.agreeNodeCount") : MAX_ACTIVE_WITNESS_NUM * 2 / 3 + 1;
    PARAMETER.agreeNodeCount = PARAMETER.agreeNodeCount > MAX_ACTIVE_WITNESS_NUM
        ? MAX_ACTIVE_WITNESS_NUM : PARAMETER.agreeNodeCount;
    if (PARAMETER.isWitness()) {
    //  INSTANCE.agreeNodeCount = MAX_ACTIVE_WITNESS_NUM * 2 / 3 + 1;
    }

    initBackupProperty(config);
    if (Constant.ROCKSDB.equals(CommonParameter
        .getInstance().getStorage().getDbEngine().toUpperCase())) {
      initRocksDbBackupProperty(config);
      initRocksDbSettings(config);
    }

    PARAMETER.actuatorSet =
        config.hasPath(Constant.ACTUATOR_WHITELIST)
            ? new HashSet<>(config.getStringList(Constant.ACTUATOR_WHITELIST))
            : Collections.emptySet();

    if (config.hasPath(Constant.NODE_METRICS_ENABLE)) {
      PARAMETER.nodeMetricsEnable = config.getBoolean(Constant.NODE_METRICS_ENABLE);
    }
    logConfig();
  }

  private static List<Witness> getWitnessesFromConfig(final com.typesafe.config.Config config) {
    return config.getObjectList(Constant.GENESIS_BLOCK_WITNESSES).stream()
        .map(Args::createWitness)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private static Witness createWitness(final ConfigObject witnessAccount) {
    final Witness witness = new Witness();
    witness.setAddress(
        Commons.decodeFromBase58Check(witnessAccount.get("address").unwrapped().toString()));
    witness.setUrl(witnessAccount.get("url").unwrapped().toString());
    witness.setVoteCount(witnessAccount.toConfig().getLong("voteCount"));
    return witness;
  }

  private static List<Account> getAccountsFromConfig(final com.typesafe.config.Config config) {
    return config.getObjectList(Constant.GENESIS_BLOCK_ASSETS).stream()
        .map(Args::createAccount)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private static Account createAccount(final ConfigObject asset) {
    final Account account = new Account();
    account.setAccountName(asset.get("accountName").unwrapped().toString());
    account.setAccountType(asset.get("accountType").unwrapped().toString());
    account.setAddress(Commons.decodeFromBase58Check(asset.get("address").unwrapped().toString()));
    account.setBalance(asset.get("balance").unwrapped().toString());
    return account;
  }

  private static RateLimiterInitialization getRateLimiterFromConfig(
      final com.typesafe.config.Config config) {

    RateLimiterInitialization initialization = new RateLimiterInitialization();
    ArrayList<RateLimiterInitialization.HttpRateLimiterItem> list1 = config
        .getObjectList(Constant.RATE_LIMITER_HTTP).stream()
        .map(RateLimiterInitialization::createHttpItem)
        .collect(Collectors.toCollection(ArrayList::new));
    initialization.setHttpMap(list1);

    ArrayList<RateLimiterInitialization.RpcRateLimiterItem> list2 = config
        .getObjectList(Constant.RATE_LIMITER_RPC).stream()
        .map(RateLimiterInitialization::createRpcItem)
        .collect(Collectors.toCollection(ArrayList::new));

    initialization.setRpcMap(list2);
    return initialization;
  }

  private static List<Node> getNodes(final com.typesafe.config.Config config, String path) {
    if (!config.hasPath(path)) {
      return Collections.emptyList();
    }
    List<Node> ret = new ArrayList<>();
    List<String> list = config.getStringList(path);
    for (String configString : list) {
      Node n = Node.instanceOf(configString);
      if (!(PARAMETER.nodeDiscoveryBindIp.equals(n.getHost())
          || PARAMETER.nodeExternalIp.equals(n.getHost())
          || "127.0.0.1".equals(n.getHost()))
          || PARAMETER.nodeListenPort != n.getPort()) {
        ret.add(n);
      }
    }
    return ret;
  }

  private static EventPluginConfig getEventPluginConfig(final com.typesafe.config.Config config) {
    EventPluginConfig eventPluginConfig = new EventPluginConfig();

    boolean useNativeQueue = false;
    int bindPort = 0;
    int sendQueueLength = 0;
    if (config.hasPath(Constant.USE_NATIVE_QUEUE)) {
      useNativeQueue = config.getBoolean(Constant.USE_NATIVE_QUEUE);

      if (config.hasPath(Constant.NATIVE_QUEUE_BIND_PORT)) {
        bindPort = config.getInt(Constant.NATIVE_QUEUE_BIND_PORT);
      }

      if (config.hasPath(Constant.NATIVE_QUEUE_SEND_LENGTH)) {
        sendQueueLength = config.getInt(Constant.NATIVE_QUEUE_SEND_LENGTH);
      }

      eventPluginConfig.setUseNativeQueue(useNativeQueue);
      eventPluginConfig.setBindPort(bindPort);
      eventPluginConfig.setSendQueueLength(sendQueueLength);
    }

    // use event plugin
    if (!useNativeQueue) {
      if (config.hasPath(Constant.EVENT_SUBSCRIBE_PATH)) {
        String pluginPath = config.getString(Constant.EVENT_SUBSCRIBE_PATH);
        if (StringUtils.isNotEmpty(pluginPath)) {
          eventPluginConfig.setPluginPath(pluginPath.trim());
        }
      }

      if (config.hasPath(Constant.EVENT_SUBSCRIBE_SERVER)) {
        String serverAddress = config.getString(Constant.EVENT_SUBSCRIBE_SERVER);
        if (StringUtils.isNotEmpty(serverAddress)) {
          eventPluginConfig.setServerAddress(serverAddress.trim());
        }
      }

      if (config.hasPath(Constant.EVENT_SUBSCRIBE_DB_CONFIG)) {
        String dbConfig = config.getString(Constant.EVENT_SUBSCRIBE_DB_CONFIG);
        if (StringUtils.isNotEmpty(dbConfig)) {
          eventPluginConfig.setDbConfig(dbConfig.trim());
        }
      }
    }

    if (config.hasPath(Constant.EVENT_SUBSCRIBE_TOPICS)) {
      List<TriggerConfig> triggerConfigList = config.getObjectList(Constant.EVENT_SUBSCRIBE_TOPICS)
          .stream()
          .map(Args::createTriggerConfig)
          .collect(Collectors.toCollection(ArrayList::new));

      eventPluginConfig.setTriggerConfigList(triggerConfigList);
    }

    return eventPluginConfig;
  }

  private static TriggerConfig createTriggerConfig(ConfigObject triggerObject) {
    if (Objects.isNull(triggerObject)) {
      return null;
    }

    TriggerConfig triggerConfig = new TriggerConfig();

    String triggerName = triggerObject.get("triggerName").unwrapped().toString();
    triggerConfig.setTriggerName(triggerName);

    String enabled = triggerObject.get("enable").unwrapped().toString();
    triggerConfig.setEnabled("true".equalsIgnoreCase(enabled));

    String topic = triggerObject.get("topic").unwrapped().toString();
    triggerConfig.setTopic(topic);

    return triggerConfig;
  }

  private static FilterQuery getEventFilter(final com.typesafe.config.Config config) {
    FilterQuery filter = new FilterQuery();
    long fromBlockLong = 0;
    long toBlockLong = 0;

    String fromBlock = config.getString(Constant.EVENT_SUBSCRIBE_FROM_BLOCK).trim();
    try {
      fromBlockLong = FilterQuery.parseFromBlockNumber(fromBlock);
    } catch (Exception e) {
      logger.error("{}", e);
      return null;
    }
    filter.setFromBlock(fromBlockLong);

    String toBlock = config.getString(Constant.EVENT_SUBSCRIBE_TO_BLOCK).trim();
    try {
      toBlockLong = FilterQuery.parseToBlockNumber(toBlock);
    } catch (Exception e) {
      logger.error("{}", e);
      return null;
    }
    filter.setToBlock(toBlockLong);

    List<String> addressList = config.getStringList(Constant.EVENT_SUBSCRIBE_CONTRACT_ADDRESS);
    addressList = addressList.stream().filter(address -> StringUtils.isNotEmpty(address)).collect(
        Collectors.toList());
    filter.setContractAddressList(addressList);

    List<String> topicList = config.getStringList(Constant.EVENT_SUBSCRIBE_CONTRACT_TOPIC);
    topicList = topicList.stream().filter(top -> StringUtils.isNotEmpty(top)).collect(
        Collectors.toList());
    filter.setContractTopicList(topicList);

    return filter;
  }

  private static void bindIp(final com.typesafe.config.Config config) {
    if (!config.hasPath(Constant.NODE_DISCOVERY_BIND_IP)
        || config.getString(Constant.NODE_DISCOVERY_BIND_IP)
        .trim().isEmpty()) {
      if (PARAMETER.nodeDiscoveryBindIp == null) {
        logger.info("Bind address wasn't set, Punching to identify it...");
        try (Socket s = new Socket("www.baidu.com", 80)) {
          PARAMETER.nodeDiscoveryBindIp = s.getLocalAddress().getHostAddress();
          logger.info("UDP local bound to: {}", PARAMETER.nodeDiscoveryBindIp);
        } catch (IOException e) {
          logger.warn("Can't get bind IP. Fall back to 0.0.0.0: " + e);
          PARAMETER.nodeDiscoveryBindIp = "0.0.0.0";
        }
      }
    } else {
      PARAMETER.nodeDiscoveryBindIp = config.getString(Constant.NODE_DISCOVERY_BIND_IP).trim();
    }
  }

  private static void externalIp(final com.typesafe.config.Config config) {
    if (!config.hasPath(Constant.NODE_DISCOVERY_EXTERNAL_IP) || config
        .getString(Constant.NODE_DISCOVERY_EXTERNAL_IP).trim().isEmpty()) {
      if (PARAMETER.nodeExternalIp == null) {
        logger.info("External IP wasn't set, using checkip.amazonaws.com to identify it...");
        BufferedReader in = null;
        try {
          in = new BufferedReader(new InputStreamReader(
              new URL(Constant.AMAZONAWS_URL).openStream()));
          PARAMETER.nodeExternalIp = in.readLine();
          if (PARAMETER.nodeExternalIp == null || PARAMETER.nodeExternalIp.trim().isEmpty()) {
            throw new IOException("Invalid address: '" + PARAMETER.nodeExternalIp + "'");
          }
          try {
            InetAddress.getByName(PARAMETER.nodeExternalIp);
          } catch (Exception e) {
            throw new IOException("Invalid address: '" + PARAMETER.nodeExternalIp + "'");
          }
          logger.info("External address identified: {}", PARAMETER.nodeExternalIp);
        } catch (IOException e) {
          PARAMETER.nodeExternalIp = PARAMETER.nodeDiscoveryBindIp;
          logger.warn(
              "Can't get external IP. Fall back to peer.bind.ip: "
                  + PARAMETER.nodeExternalIp + " :"
                  + e);
        } finally {
          if (in != null) {
            try {
              in.close();
            } catch (IOException e) {
              //ignore
            }
          }

        }
      }
    } else {
      PARAMETER.nodeExternalIp = config.getString(Constant.NODE_DISCOVERY_EXTERNAL_IP).trim();
    }
  }

  private static void initRocksDbSettings(Config config) {
    String prefix = Constant.STORAGE_DB_SETTING;
    int levelNumber = config.hasPath(prefix + "levelNumber")
        ? config.getInt(prefix + "levelNumber") : 7;
    int compactThreads = config.hasPath(prefix + "compactThreads")
        ? config.getInt(prefix + "compactThreads")
        : max(Runtime.getRuntime().availableProcessors(), 1);
    int blocksize = config.hasPath(prefix + "blocksize")
        ? config.getInt(prefix + "blocksize") : 16;
    long maxBytesForLevelBase = config.hasPath(prefix + "maxBytesForLevelBase")
        ? config.getInt(prefix + "maxBytesForLevelBase") : 256;
    double maxBytesForLevelMultiplier = config.hasPath(prefix + "maxBytesForLevelMultiplier")
        ? config.getDouble(prefix + "maxBytesForLevelMultiplier") : 10;
    int level0FileNumCompactionTrigger =
        config.hasPath(prefix + "level0FileNumCompactionTrigger") ? config
            .getInt(prefix + "level0FileNumCompactionTrigger") : 2;
    long targetFileSizeBase = config.hasPath(prefix + "targetFileSizeBase") ? config
        .getLong(prefix + "targetFileSizeBase") : 64;
    int targetFileSizeMultiplier = config.hasPath(prefix + "targetFileSizeMultiplier") ? config
        .getInt(prefix + "targetFileSizeMultiplier") : 1;

    PARAMETER.rocksDBCustomSettings = RocksDbSettings
        .initCustomSettings(levelNumber, compactThreads, blocksize, maxBytesForLevelBase,
            maxBytesForLevelMultiplier, level0FileNumCompactionTrigger,
            targetFileSizeBase, targetFileSizeMultiplier);
    RocksDbSettings.loggingSettings();
  }

  private static void initRocksDbBackupProperty(Config config) {
    boolean enable =
        config.hasPath(Constant.STORAGE_BACKUP_ENABLE)
            && config.getBoolean(Constant.STORAGE_BACKUP_ENABLE);
    String propPath = config.hasPath(Constant.STORAGE_BACKUP_PROP_PATH)
        ? config.getString(Constant.STORAGE_BACKUP_PROP_PATH) : "prop.properties";
    String bak1path = config.hasPath(Constant.STORAGE_BACKUP_BAK1PATH)
        ? config.getString(Constant.STORAGE_BACKUP_BAK1PATH) : "bak1/database/";
    String bak2path = config.hasPath(Constant.STORAGE_BACKUP_BAK2PATH)
        ? config.getString(Constant.STORAGE_BACKUP_BAK2PATH) : "bak2/database/";
    int frequency = config.hasPath(Constant.STORAGE_BACKUP_FREQUENCY)
        ? config.getInt(Constant.STORAGE_BACKUP_FREQUENCY) : 10000;
    PARAMETER.dbBackupConfig = DbBackupConfig.getInstance()
        .initArgs(enable, propPath, bak1path, bak2path, frequency);
  }

  private static void initBackupProperty(Config config) {
    PARAMETER.backupPriority = config.hasPath(Constant.NODE_BACKUP_PRIORITY)
        ? config.getInt(Constant.NODE_BACKUP_PRIORITY) : 0;

    PARAMETER.backupPort = config.hasPath(Constant.NODE_BACKUP_PORT)
        ? config.getInt(Constant.NODE_BACKUP_PORT) : 10001;

    PARAMETER.keepAliveInterval = config.hasPath(Constant.NODE_BACKUP_KEEPALIVEINTERVAL)
        ? config.getInt(Constant.NODE_BACKUP_KEEPALIVEINTERVAL) : 3000;

    PARAMETER.backupMembers = config.hasPath(Constant.NODE_BACKUP_MEMBERS)
        ? config.getStringList(Constant.NODE_BACKUP_MEMBERS) : new ArrayList<>();
  }

  public static void logConfig() {
    CommonParameter parameter = CommonParameter.getInstance();
    logger.info("\n");
    logger.info("************************ Net config ************************");
    logger.info("P2P version: {}", parameter.getNodeP2pVersion());
    logger.info("Bind IP: {}", parameter.getNodeDiscoveryBindIp());
    logger.info("External IP: {}", parameter.getNodeExternalIp());
    logger.info("Listen port: {}", parameter.getNodeListenPort());
    logger.info("Discover enable: {}", parameter.isNodeDiscoveryEnable());
    logger.info("Active node size: {}", parameter.getActiveNodes().size());
    logger.info("Passive node size: {}", parameter.getPassiveNodes().size());
    logger.info("FastForward node size: {}", parameter.getFastForwardNodes().size());
    logger.info("Seed node size: {}", parameter.getSeedNode().getIpList().size());
    logger.info("Max connection: {}", parameter.getNodeMaxActiveNodes());
    logger.info("Max connection with same IP: {}", parameter.getNodeMaxActiveNodesWithSameIp());
    logger.info("Solidity threads: {}", parameter.getSolidityThreads());
    logger.info("************************ Backup config ************************");
    logger.info("Backup priority: {}", parameter.getBackupPriority());
    logger.info("Backup listen port: {}", parameter.getBackupPort());
    logger.info("Backup listen keepAliveInterval: {}", parameter.getKeepAliveInterval());
    logger.info("Backup member size: {}", parameter.getBackupMembers().size());
    logger.info("************************ Code version *************************");
    logger.info("Code version : {}", Version.getVersion());
    logger.info("Version name: {}", Version.versionName);
    logger.info("Version code: {}", Version.versionCode);
    logger.info("************************ DB config *************************");
    logger.info("DB version : {}", parameter.getStorage().getDbVersion());
    logger.info("DB engine : {}", parameter.getStorage().getDbEngine());
    logger.info("***************************************************************");
    logger.info("\n");
  }

<<<<<<< HEAD
  public static void initEncryptoEngine(Args cfgArgs) {
    DBConfig.setECKeyCryptoEngine(cfgArgs.isEckey());
  }
  public static void initDBConfig(Args cfgArgs) {
    if (Objects.nonNull(cfgArgs.getStorage())) {
      DBConfig.setDbVersion(cfgArgs.getStorage().getDbVersion());
      DBConfig.setDbEngine(cfgArgs.getStorage().getDbEngine());
      DBConfig.setPropertyMap(cfgArgs.getStorage().getPropertyMap());
      DBConfig.setDbSync(cfgArgs.getStorage().isDbSync());
      DBConfig.setDbDirectory(cfgArgs.getStorage().getDbDirectory());
    }

    if (Objects.nonNull(cfgArgs.getGenesisBlock())) {
      DBConfig.setBlocktimestamp(cfgArgs.getGenesisBlock().getTimestamp());
      DBConfig.setGenesisBlock(cfgArgs.getGenesisBlock());
    }

    DBConfig.setOutputDirectoryConfig(cfgArgs.getOutputDirectory());
    DBConfig.setRocksDbSettings(cfgArgs.getRocksDBCustomSettings());
    DBConfig.setAllowMultiSign(cfgArgs.getAllowMultiSign());
    DBConfig.setMaintenanceTimeInterval(cfgArgs.getMaintenanceTimeInterval());
    DBConfig.setAllowAdaptiveEnergy(cfgArgs.getAllowAdaptiveEnergy());
    DBConfig.setAllowDelegateResource(cfgArgs.getAllowDelegateResource());
    DBConfig.setAllowTvmTransferTrc10(cfgArgs.getAllowTvmTransferTrc10());
    DBConfig.setAllowTvmConstantinople(cfgArgs.getAllowTvmConstantinople());
    DBConfig.setAllowTvmSolidity059(cfgArgs.getAllowTvmSolidity059());
    DBConfig.setForbidTransferToContract(cfgArgs.getForbidTransferToContract());
    DBConfig.setAllowSameTokenName(cfgArgs.getAllowSameTokenName());
    DBConfig.setAllowCreationOfContracts(cfgArgs.getAllowCreationOfContracts());
//    DBConfig.setAllowShieldedTransaction(cfgArgs.getAllowShieldedTransaction());
    DBConfig.setAllowShieldedTRC20Transaction(
        cfgArgs.getAllowShieldedTRC20Transaction());
    DBConfig.setAllowAccountStateRoot(cfgArgs.getAllowAccountStateRoot());
    DBConfig.setAllowProtoFilterNum(cfgArgs.getAllowProtoFilterNum());
    DBConfig.setProposalExpireTime(cfgArgs.getProposalExpireTime());
    DBConfig.setBlockNumForEneryLimit(cfgArgs.getBlockNumForEneryLimit());
    DBConfig.setFullNodeAllowShieldedTransaction(cfgArgs.isFullNodeAllowShieldedTransactionArgs());
    DBConfig.setZenTokenId(cfgArgs.getZenTokenId());
    DBConfig.setCheckFrozenTime(cfgArgs.getCheckFrozenTime());
    DBConfig.setValidContractProtoThreadNum(cfgArgs.getValidContractProtoThreadNum());
    DBConfig.setVmTrace(cfgArgs.isVmTrace());
    DBConfig.setDebug(cfgArgs.isDebug());
    DBConfig.setMinTimeRatio(cfgArgs.getMinTimeRatio());
    DBConfig.setMaxTimeRatio(cfgArgs.getMaxTimeRatio());
    DBConfig.setSolidityNode(cfgArgs.isSolidityNode());
    DBConfig.setSupportConstant(cfgArgs.isSupportConstant());
    DBConfig.setLongRunningTime(cfgArgs.getLongRunningTime());
    DBConfig.setChangedDelegation(cfgArgs.getChangedDelegation());
    DBConfig.setActuatorSet(cfgArgs.getActuatorSet());
//    DBConfig.setECKeyCryptoEngine(cfgArgs.isECKeyCryptoEngine());
    DBConfig.setECKeyCryptoEngine(cfgArgs.isEckey());
  }

  public void setFullNodeAllowShieldedTransaction(boolean fullNodeAllowShieldedTransaction) {
    this.fullNodeAllowShieldedTransactionArgs = fullNodeAllowShieldedTransaction;
    DBConfig.setFullNodeAllowShieldedTransaction(fullNodeAllowShieldedTransaction);
  }

  /**
   * Get storage path by name of database
   *
   * @param dbName name of database
   * @return path of that database
   */
  public String getOutputDirectoryByDbName(String dbName) {
    String path = storage.getPathByDbName(dbName);
    if (!StringUtils.isBlank(path)) {
      return path;
    }
    return getOutputDirectory();
=======
  public static void setFullNodeAllowShieldedTransaction(boolean fullNodeAllowShieldedTransaction) {
    PARAMETER.fullNodeAllowShieldedTransactionArgs = fullNodeAllowShieldedTransaction;
>>>>>>> develop
  }

  /**
   * get output directory.
   */
  public String getOutputDirectory() {
    if (!this.outputDirectory.equals("") && !this.outputDirectory.endsWith(File.separator)) {
      return this.outputDirectory + File.separator;
    }
    return this.outputDirectory;
  }
}

<<<<<<< HEAD
  public ECKey getMyKey() {
    if (StringUtils.isEmpty(INSTANCE.p2pNodeId)) {
      INSTANCE.p2pNodeId = getGeneratedNodePrivateKey();
    }

    return ECKey.fromPrivate(Hex.decode(INSTANCE.p2pNodeId));
  }

  public boolean isECKeyCryptoEngine() {
//    return cryptoEngine.equalsIgnoreCase(Constant.ECKey_ENGINE);
    return isEckey;
  }
}
=======
>>>>>>> develop
