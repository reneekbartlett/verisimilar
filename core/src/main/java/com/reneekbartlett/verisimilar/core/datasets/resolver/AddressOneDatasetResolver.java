//package com.reneekbartlett.verisimilar.core.datasets.resolver;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.reneekbartlett.verisimilar.core.datasets.key.AddressOneDatasetKey;
//import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
//import com.reneekbartlett.verisimilar.core.datasets.result.AddressOneDatasetResult;
//
//// TODO:  Maybe make this a special type that combines DatasetProviders?
//public class AddressOneDatasetResolver extends AbstractDatasetResolver<AddressOneDatasetKey, AddressOneDatasetResult> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AddressOneDatasetResolver.class);
//    private static final String TEMPLATES_FILE = "";
//
//    public AddressOneDatasetResolver(ResourceLoaderUtil loader) {
//        super(loader);
//    }
//
//    @Override
//    public AddressOneDatasetResult loadForKey(AddressOneDatasetKey key) {
//        //Map<String, Double> all = loadDataset(key);
//        String[] templates = loadTemplates(TEMPLATES_FILE);
//        return new AddressOneDatasetResult(templates);
//    }
//
//    @Override
//    public Class<AddressOneDatasetKey> keyType() {
//        return AddressOneDatasetKey.class;
//    }
//
//    private String[] loadTemplates(String path) {
//        return loader.loadStringArray(path);
//    }
//}
