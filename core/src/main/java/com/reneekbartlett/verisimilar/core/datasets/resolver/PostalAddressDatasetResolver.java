//package com.reneekbartlett.verisimilar.core.datasets.resolver;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.reneekbartlett.verisimilar.core.datasets.key.PostalAddressDatasetKey;
//import com.reneekbartlett.verisimilar.core.datasets.loader.ResourceLoaderUtil;
//import com.reneekbartlett.verisimilar.core.datasets.result.PostalAddressDatasetResult;
//
///***
// * 
// */
//public class PostalAddressDatasetResolver extends AbstractDatasetResolver<PostalAddressDatasetKey, PostalAddressDatasetResult> {
//
//    @SuppressWarnings("unused")
//    private static final Logger LOGGER = LoggerFactory.getLogger(PostalAddressDatasetResolver.class);
//
//    public PostalAddressDatasetResolver(ResourceLoaderUtil loader) {
//        super(loader);
//    }
//
//    @Override
//    public PostalAddressDatasetResult loadForKey(PostalAddressDatasetKey key) {
//        Map<String, Double> address1_templates = new HashMap<>(); //loadTypeDataset("disposable", key);
//        Map<String, Double> address2_templates = new HashMap<>();
//
//        return new PostalAddressDatasetResult(address1_templates, address2_templates);
//    }
//
//    @Override
//    public Class<PostalAddressDatasetKey> keyType() {
//        return PostalAddressDatasetKey.class;
//    }
//
//}