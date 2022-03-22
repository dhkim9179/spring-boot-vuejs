package com.dhkim.prj.admin.core.support.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingResolver extends PageableHandlerMethodArgumentResolver {
	
	private static final SortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new SortHandlerMethodArgumentResolver();
	private SortArgumentResolver sortResolver;
	
	public PagingResolver() {
		this((SortArgumentResolver) null);
	}

	public PagingResolver(SortHandlerMethodArgumentResolver sortResolver) {
		this((SortArgumentResolver) sortResolver);
	}

	public PagingResolver(@Nullable SortArgumentResolver sortResolver) {
		this.sortResolver = sortResolver == null ? DEFAULT_SORT_RESOLVER : sortResolver;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Pageable.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Pageable resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		
		String page = Integer.toString(((Integer.parseInt(webRequest.getParameter("page"))-1) * 10));
		String pageSize = webRequest.getParameter("size");

		Sort sort = sortResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		Pageable pageable = getPageable(parameter, page, pageSize);

		if (sort.isSorted()) {
			return PageRequest.of((pageable.getPageNumber()), pageable.getPageSize(), sort);
		}

		return pageable;
	}

}
