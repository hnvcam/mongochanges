package com.aiwsolutions.mongo.changes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by camhoang on 11/22/15.
 */
@Component
public class ChangeSetLoader {
    private static final String JS_PATTERN = "/**/*.js";
    private static final String CLASS_PATTERN = "/**/*.class";

    @Autowired
    private ApplicationContext applicationContext;

    private MetadataReaderFactory metadataReaderFactory;

    public List<Class<? extends ChangeSet>> loadChangeSetClassesFromPackage(String packageName) throws IOException {
        String path = resolveBasePackage(packageName);
        List<Class<? extends ChangeSet>> result = new ArrayList<>();
        Resource[] resources = applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path + CLASS_PATTERN);
        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                continue;
            }
            MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
            Class clazz = getChangeSetClass(metadataReader);
            if (clazz != null) {
                result.add(clazz);
            }
        }
        return result;
    }

    private Class getChangeSetClass(MetadataReader metadataReader) {
        try {
            Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
            if (ChangeSet.class.isAssignableFrom(clazz)) {
                return clazz;
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    public List<File> loadFilesFromPath(String path) throws IOException {
        Resource[] resources = applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path + JS_PATTERN);
        List<File> result = new ArrayList<>();
        for (Resource resource : resources) {
            File file = resource.getFile();
            if (!file.isFile()) {
                continue;
            }
            result.add(file);
        }
        return result;
    }

    protected MetadataReaderFactory getMetadataReaderFactory() {
        if (metadataReaderFactory == null) {
            metadataReaderFactory = new CachingMetadataReaderFactory(applicationContext);
        }
        return metadataReaderFactory;
    }
}
