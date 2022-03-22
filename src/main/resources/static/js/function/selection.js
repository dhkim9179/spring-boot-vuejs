Vue.component("selection", {
  template: '\
    <div class="form-floating">\
      <select class="form-select" name="search" v-model="search">\
		<option value = "" selected>{{ codeMessage }}</option>\
        <option v-for="code in codeList" :key="code.id" :value="code.id">{{ code.name }}</option>\
      </select>\
      <label for="">{{ label }}</label>\
    </div>\
          ',

  props: ['label', 'type', 'codeId', 'condition', 'message'],
  data: function() {
    return {
      selectType: '',
      selectCodeId: '',
      search: '',
      codeList: [],

      codeMessage: '',
    }
  },

  methods: {
    getCommonCodeList: function(codeId) {
		// 공통코드 목록 가져오기
		axios.get(contextPath + '/commoncode/' + codeId)
		.then((res) => {
			this.codeList = res.data.codeList;
		})
	},

  },

  mounted() {
    this.search = this.condition;
    this.selectType = this.type;
    this.selectCodeId = this.codeId;
	this.codeMessage = this.message;
  },

  watch: {
    condition: function() {
      this.search = this.condition;
    },

    search: function() {
      this.$emit('result', this.search);
    },

	'$i18n.locale'() {
		this.codeMessage = this.message;
	},

    selectType: function() {
      if(this.selectType == 'COMMON_CODE') {
        this.getCommonCodeList(this.selectCodeId);
      }
    },
  },

})
