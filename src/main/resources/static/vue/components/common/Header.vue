<template>
	<div id="top">
		<h1 style="color:white" @click="gotoMainPage()">{{ $t("message.header") }}</h1>
		<form id="logoutForm" name="logoutForm" methods="post" action="/admin/logout" @submit.prevent="onSubmit()">
			<div class="row">
        <div class="col-10">

        </div>
        <div class="col-1">
					<select class="form-select" v-on:change="changeLanguage()" v-model="lang">
						<option v-for="language in languages" :key="language.id" v-bind:value="language.id">{{ languageName(language.id) }}</option>
					</select>
				</div>
				<div class="col-1 mr-3">
					<button type="submit" class="btn btn-primary" id="btnLogin" style="float: right;">{{ $t("message.logout") }}</button>
				</div>
			</div>
		</form>
	</div>
</template>

<script>
module.exports = {
	data : function() {
		return {
			languages: [],
			lang: '',
		}
	},
	methods: {
		gotoMainPage: function() {
				this.$router.replace('/');
		},
    languageName: function(id) {
      var message = "message."+id
			return this.$t(message)
    },
    changeLanguage: function() {
			this.$i18n.locale = this.lang;
		},
		onSubmit: function() {
			axios.post(contextPath + '/logout');
		}
	},
  created () {
		this.lang = language;
		axios.get(contextPath + '/languages')
    .then(res => {
			this.languages = res.data.languages;
		})
	},
}
</script>

<style scoped>
#top {
	width: 90%;
	height: 120px;
	text-align:center;
	background-color: #afeeee;
}

h1 {
	text-align: center;
}

.selectbox {
  position: relative;
  width: 200px;
  border: 1px solid #999;
  z-index: 1;
}

.selectbox:before {
  content: "";
  position: absolute;
  top: 50%;
  right: 15px;
  width: 0;
  height: 0;
  margin-top: -1px;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 5px solid #333;
}

.selectbox label {
  position: absolute;
  top: 1px;
  left: 5px;
  padding: .8em .5em;
  color: #999;
  z-index: -1;
}

.selectbox select {
  width: 100%;
  height: auto;
  line-height: normal;
  font-family: inherit;
  padding: .8em .5em;
  border: 0;
  opacity: 0;
  filter:alpha(opacity=0);
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}
</style>
