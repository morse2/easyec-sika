package com.googlecode.easyec.sika.mappings;

import com.googlecode.easyec.sika.WorkingException;
import com.googlecode.easyec.sika.support.WorkbookStrategy;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class AnnotationMappingResolverChain<IN extends AnnotationMappingParam<T>, T> implements AnnotationMappingResolver<IN, Void> {

    private List<AnnotationMappingParamResolver<IN, T>> resolvers = new ArrayList<>();

    public AnnotationMappingResolverChain(AnnotationMappingParamResolver<IN, T>... resolvers) {
        Assert.notNull(resolvers, "AnnotationMappingParamResolver(s) must't be null.");

        Stream.of(resolvers).forEach(getResolvers()::add);
    }

    public List<AnnotationMappingParamResolver<IN, T>> getResolvers() {
        return resolvers;
    }

    @Override
    public Void perform(int rowIndex, WorkbookStrategy strategy, IN data) throws WorkingException {
        for (AnnotationMappingParamResolver<IN, T> resolver : getResolvers()) {
            Boolean continued = resolver.perform(rowIndex, strategy, data);
            if (continued == null || !continued) break;
        }

        return null;
    }
}
