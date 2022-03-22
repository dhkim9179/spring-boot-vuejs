// Vue Validator
Vue.use(VeeValidate);

// 다국어
const i18n = new VueI18n({
  locale: language, // set locale
  messages, // set locale messages
})

// Axios Response Global Configuration
axios.interceptors.response.use(
  function (response) {
    // console.log(response);
    if(response.config.url == contextPath + '/logout') {
      location.href = contextPath + "/login";
    }
    else {
      // JSON 응답처리
      if(response.data.result.code != '0000') {
        if(response.data.result.code == '0400') {
           location.href = contextPath + "/error?code=400";
        }
        else if(response.data.result.code == '0401') {
           location.href = contextPath + "/login";
        }
        else if(response.data.result.code == '0403') {
           location.href = contextPath + "/error?code=403";
        } else {
          location.href = contextPath + "/error?code=9999";
        }
      }
    }
    return response;
  },
  function (error) {
    location.href = contextPath + "/error?code=" + error.response.status;
    return Promise.reject(error);
  }
);

var admin = new Vue({
	el: '#admin',
	components: {
    	'header-view': httpVueLoader(contextPath + '/vue/components/common/Header.vue'),
		'menu-view': httpVueLoader(contextPath + '/vue/components/common/Menu.vue'),
	},
	
	mounted() {
		// 권한에 맞는 리소스 가져오기
		axios.get(contextPath + '/resources').then(res => {
			// local storage에 권한 저장
			for(var i=0;i<res.data.resources.length;i++) {
				sessionStorage.setItem(res.data.resources[i].url, res.data.resources[i].authority);
			}
		});
	
		// 브라우저 창 닫을 때 로컬 저장소 초기화
		window.onbeforeunload = function (e) {
			sessionStorage.clear();
		}
	},

	router: router, // 라우터
	i18n // 다국어
});