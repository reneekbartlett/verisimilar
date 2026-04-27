//package com.reneekbartlett.verisimilar.core.io;
//
//import org.jspecify.annotations.Nullable;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//
//public class ResourceFileLoader implements ResourceLoaderAware {
//
//    private ResourceLoader resourceLoader;
//
//    @Override
//    public void setResourceLoader(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }
//
//    public Resource getResource(String location){
//        return resourceLoader.getResource(location);
//    }
//
//}
