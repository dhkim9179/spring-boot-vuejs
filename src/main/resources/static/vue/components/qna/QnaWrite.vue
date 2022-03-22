<template>
	<div class="container">
		<h2 v-show="showTitle">{{ $t("message.qnaRegister") }}</h2>
		<h2 v-show="!showTitle">{{ $t("message.qnaRegister") }}</h2>
		<form @submit.prevent="validate">

      <div class="card-body p-2">
				<div class="form-group form-floating mt-2 mb-2">
          <selection
            :label='$t("message.questionCategory")'
						:type='"COMMON_CODE"'
						:code-id='"QNA_CATEGORY"'
						:condition='questionCategory'
            :message='$t("message.selectData")'
						@result="getCategory"
					></selection>
				</div>
			</div>

			<div class="card-body p-2">
				<div class="form-group form-floating mt-2 mb-2">
				  <input type="text" name="questionTitle" v-model="questionTitle" class="form-control" v-validate="'required|max:30'">
					<label>{{ $t("message.questionTitle") }}</label>
				</div>
				<span class="text-danger">{{ errors.first('questionTitle') }}</span>
			</div>

      <div class="card-body p-2">
         <div class="form-group form-floating mt-2 mb-2">
           <textarea name="questionContent" v-model="questionContent" class="form-control" v-validate="'required|max:500'"></textarea>
           <label>{{ $t("message.questionContent") }}</label>
         </div>
         <span class="text-danger">{{ errors.first('questionContent') }}</span>
      </div>

			<div class="card-body p-2" v-show="showAnswer">
         <div class="form-group form-floating mt-2 mb-2">
           <textarea name="answer" v-model="answer" class="form-control" v-validate="'max:500'"></textarea>
           <label>{{ $t("message.questionAnswer") }}</label>
         </div>
      </div>

			<div class="card-body p-2">
				<div class="mt-2 mb-2">
					<button type="submit" class="btn btn-primary">{{ $t("message.save") }}</button>
					<button @click="back()" class="btn btn-primary">{{ $t("message.list") }}</button>
				</div>
			</div>
		</form>
	</div>
</template>

<script>
module.exports = {
	data: function() {
		return {
			qnaSno: '',
			questionCategory: '',
      questionTitle: '',
      questionContent: '',
			answer: '',
			showAnswer: false,
			showTitle: true,
		}
	},

	methods: {
		register: function() {

      if (this.questionCategory == "") {
        alert(this.$t("message.selectCategory"));
        return;
      }

			if (this.qnaSno != null) {
				axios.put(contextPath + '/qna/' + this.qnaSno, {
					questionCategory: this.questionCategory,
          questionTitle: this.questionTitle,
          questionContent: this.questionContent,
					answer: this.answer
				}).then(res => {
					alert(this.$t("message.success"));
					this.back();
				})
			} else {
				axios.post(contextPath + '/qna', {
					questionCategory: this.questionCategory,
          questionTitle: this.questionTitle,
          questionContent: this.questionContent
				}).then(res => {
					alert(this.$t("message.success"));
					this.back();
				})
			}

		},

		back: function() {
			this.$router.replace('/qna');
		},

		// 입력값 유효성 검증
		validate() {
      this.$validator.validateAll()
       .then((result) => {
          if (result) {
						this.register();
          }
      });
    },

    getCategory: function(questionCategory) {
      this.questionCategory = questionCategory;
    }
	},

	created() {
		this.showAnswer = false;
		this.showTitle = true;
		this.qnaSno = this.$route.query.qnaSno;

		/**
		 * 일련번호 유무에 따라 등록 또는 수정 페이지로 동작
		*/
		if (this.qnaSno != null) {
			this.showAnswer = true;
			this.showTitle = false;
			axios.get(contextPath + '/qna/' + this.qnaSno)
			.then(res => {
				this.questionCategory = res.data.qna.questionCategory;
				this.questionTitle = res.data.qna.questionTitle;
				this.questionContent = res.data.qna.questionContent;
				this.answer = res.data.qna.answer;
			})
		}
	}
}
</script>
