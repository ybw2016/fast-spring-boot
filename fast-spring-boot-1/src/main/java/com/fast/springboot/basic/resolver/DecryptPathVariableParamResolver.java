package com.fast.springboot.basic.resolver;

import com.fast.springboot.basic.annotation.DecryptPathVariableRequestParam;
import com.fast.springboot.basic.util.Base64Util;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.method.support.UriComponentsContributor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义PathVariable解析器
 *
 * @author bowen.yan
 * @date 2018-11-20
 */
@Slf4j
public class DecryptPathVariableParamResolver extends AbstractNamedValueMethodArgumentResolver implements UriComponentsContributor {
    private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (!parameter.hasParameterAnnotation(DecryptPathVariableRequestParam.class)) {
            return false;
        }
        if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
            String paramName = parameter.getParameterAnnotation(DecryptPathVariableRequestParam.class).value();
            return StringUtils.hasText(paramName);
        }
        return true;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        DecryptPathVariableRequestParam annotation = parameter.getParameterAnnotation(DecryptPathVariableRequestParam.class);
        return new DecryptPathVariableParamResolver.DecryptPathVariableRequestParamNamedValueInfo(annotation);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Map<String, String> uriTemplateVars = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

        Object decrypted = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE + "_DECRYPT", RequestAttributes.SCOPE_REQUEST);
        if (decrypted == null) {
            for (Map.Entry<String, String> entry : uriTemplateVars.entrySet()) {
                String rawValue = entry.getValue();
                String newValue = Base64Util.decrypt(rawValue);
                log.info("DecryptPathVariableParamResolver -> key:{}, rawValue:{}, newValue:{}", entry.getKey(), rawValue, newValue);
                entry.setValue(newValue);
            }
            request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE + "_DECRYPT", true, RequestAttributes.SCOPE_REQUEST);
        }

        return (uriTemplateVars != null ? uriTemplateVars.get(name) : null);
    }

    @Override
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
        throw new MissingPathVariableException(name, parameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleResolvedValue(Object arg, String name, MethodParameter parameter,
                                       ModelAndViewContainer mavContainer, NativeWebRequest request) {
        String key = View.PATH_VARIABLES;
        int scope = RequestAttributes.SCOPE_REQUEST;
        Map<String, Object> pathVars = (Map<String, Object>) request.getAttribute(key, scope);
        if (pathVars == null) {
            pathVars = new HashMap<String, Object>();
            request.setAttribute(key, pathVars, scope);
        }
        pathVars.put(name, arg);
    }

    @Override
    public void contributeMethodArgument(MethodParameter parameter, Object value,
                                         UriComponentsBuilder builder, Map<String, Object> uriVariables,
                                         ConversionService conversionService) {
        if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
            return;
        }

        DecryptPathVariableRequestParam ann = parameter.getParameterAnnotation(DecryptPathVariableRequestParam.class);
        String name = (ann != null && !StringUtils.isEmpty(ann.value()) ? ann.value() : parameter.getParameterName());
        value = formatUriValue(conversionService, new TypeDescriptor(parameter.nestedIfOptional()), value);
        uriVariables.put(name, value);
    }

    protected String formatUriValue(ConversionService cs, TypeDescriptor sourceType, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else if (cs != null) {
            return (String) cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
        } else {
            return value.toString();
        }
    }

    private static class DecryptPathVariableRequestParamNamedValueInfo extends NamedValueInfo {
        public DecryptPathVariableRequestParamNamedValueInfo(DecryptPathVariableRequestParam annotation) {
            super(annotation.name(), annotation.required(), ValueConstants.DEFAULT_NONE);
        }
    }
}
