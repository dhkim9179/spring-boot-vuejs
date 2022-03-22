var router = new VueRouter({
	routes: [
		
		/**
		 * 존재하지 않는 페이지
		 */
		{
			path: '*',
			beforeEnter: (to, from, next) => {
				// 404 에러페이지
        		location.href = contextPath + "/error?code=404";
			}
      	},
		
		/**
		 * 최초화면
		 */
		{
			path: '/',
			component : httpVueLoader(contextPath + '/vue/components/common/Main.vue')
		},
		
	
		/**
		 * qna 화면  
		 */
		{
			path: '/qna',
			component : httpVueLoader(contextPath + '/vue/components/qna/Qna.vue')
		},
		
		{
			path:'/qna/write',
			name:'write_qna',
			component: httpVueLoader(contextPath + '/vue/components/qna/QnaWrite.vue')
		},
		
		/**
		 * 법인 화면  
		 */
		{
			path: '/company',
			component : httpVueLoader(contextPath + '/vue/components/company/Company.vue')
		},
		
		/**
		 * 사용자 화면  
		 */
		{
			path: '/system/user',
			component : httpVueLoader(contextPath + '/vue/components/system/user/User.vue')
		},
	]
});