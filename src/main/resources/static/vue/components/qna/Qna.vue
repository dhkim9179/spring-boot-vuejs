<template>
  <div class="container">
    <form id="searchForm" name="searchForm" class="wizard-big form-horizonal">
      <table class="table table-sm">
        <colgroup>
  				<col style="width: 10%;"/>
  				<col style="width: auto;"/>
  				<col style="width: 10%;"/>
  				<col style="width: auto;"/>
  			</colgroup>
        <tbody>
				<tr>
					<th class="text-center align-middle">{{ $t("message.questionTitle") }}</th>
					<td>
						<div class="input-group col-lg-5">
							<input type="text" class="form-control" v-model.trim="questionTitle"/>
						</div>
					</td>

					<th class="text-center align-middle">{{ $t("message.questionAnswerYn") }}</th>
					<td>
						<div class="col-lg-5">
							<select v-model="answer" class="form-select">
								<option value="">{{ $t("message.all") }}</option>
								<option value="Y">{{ $t("message.questionAnswerY") }}</option>
								<option value="N">{{ $t("message.questionAnswerN") }}</option>
							</select>
						</div>
					</td>
				</tr>

        <tr>
          <th class="text-center align-middle">{{ $t("message.questionWriteDate") }}</th>
          <td>
            <datepicker
							@start-date="setStartDate"
							@end-date="setEndDate"
						></datepicker>
          </td>
        </tr>
			</tbody>
      </table>
    </form>

    <div class="position-relative">
			<div class="row align-items-end mt-5 mb-2">
				<div class="col-auto">
					<form>
						<select class="form-select-sm" style="width: auto; height: auto;" v-model="pageSize">
							<option value="10" selected>10</option>
							<option value="15">15</option>
							<option value="20">20</option>
							<option value="30">30</option>
						</select>
						<span><label>{{ $t("message.sizeandpage") }}</label></span>
            <span><label>({{ $t("message.total") }} {{ totalCount }} {{ $t("message.count") }})</label></span>
					</form>
				</div>
				<div class="col-auto me-auto"></div>
				<div class="col-auto">
          <button type="button" class="btn-secondary" @click="goNew()">{{ $t("message.new") }}</button>
    			<button type="button" class="btn-secondary" @click="getData(questionTitle, answer, startDate, endDate, page, pageSize)">{{ $t("message.search") }}</button>
				</div>
			</div>
		</div>

    <table class="table">
      <thead class="table-bordered">
  			<tr>
  				<th class="text-center">{{ $t("message.questionCategory") }}</th>
  				<th class="text-center">{{ $t("message.questionTitle") }}</th>
  				<th class="text-center">{{ $t("message.questionWriteDate") }}</th>
  				<th class="text-center">{{ $t("message.questionWriter") }}</th>
  				<th class="text-center">{{ $t("message.questionAnswerYn") }}</th>
  				<th class="text-center">{{ $t("message.questionAnswerDatetime") }}</th>
  				<th class="text-center">{{ $t("message.questionAnswerWriter") }}</th>
  			</tr>
  		</thead>
			<tbody>
				<!-- [D]검색결과 없을 때 -->
				<tr v-show="!result">
					<td colspan="7">
						<p class="text-center">{{ $t("message.noResult") }}</p>
					</td>
				</tr>
				<!-- [D]검색결과 있을 때 -->
					<tr v-show="result" v-for="qna in qnaList" :key="qna.qnaSno" @click="goModification(qna.qnaSno)">
						<td align="center">{{ qna.questionCategory }}</td>
						<td align="center">{{ qna.questionTitle }}</td>
						<td align="center">{{ getDatetimeFormat(qna.createDatetime) }}</td>
						<td align="center">{{ qna.createUserId }}</td>
						<td align="center">{{ qna.answer }}</td>
            <td align="center">{{ getDatetimeFormat(qna.answerDatetime) }}</td>
            <td align="center">{{ qna.answerUserId }}</td>
					</tr>
			</tbody>
		</table>

    <paging
			:currentpage='page'
			:pagesize='pageSize'
			:lastpage='lastPage'
			@page-event="getPageData">
		</paging>
  </div>
</template>

<script>
module.exports = {
	data: function() {
		return {
			// 검색조건
			questionTitle: '',
			answer: '',
			startDate: '',
      endDate: '',

			// 검색결과
			qnaList: [],
			totalCount: '',
			result: false,

			// 페이징
			page: 1,
			lastPage: '',
			pageSize: 10, // row size
		}
	},

  methods: {
		/**
		 * 서버에서 데이터 가져오기
		 */
		getData: function(questionTitle, answer, startDate, endDate, page, size) {
			if(questionTitle == null) {
				questionTitle = "";
			}

			this.page = page;
			this.pageSize = size;

			// 사용자 목록 가져오기
			axios.get(contextPath + '/qna', {
				params: {
					questionTitle: questionTitle,
					answer: answer,
					startDate: startDate.replaceAll('-', ''),
					endDate: endDate.replaceAll('-', ''),
					page: page,
					size: size
				}
			}).then((res) => {
				this.qnaList = res.data.qnaList;
				this.totalCount = res.data.totalCount;
        this.initSearchCondition();
				if(this.totalCount == 0) {
					this.lastPage = 1;
				}
				else {
					this.lastPage = Math.ceil(this.totalCount / this.pageSize);
				}
			})
		},

		goNew: function() {
			this.$router.push('/qna/write');
		},

		/**
			* 페이징 검색 데이터
		*/
		getPageData: function(page) {
			this.getData(this.questionTitle, this.answer, this.startDate, this.endDate, page, this.pageSize);
		},

		/**
			* 검색결과
		*/
		showQnaList: function() {
			this.result = true;
			if(this.qnaList == null || this.qnaList.length == 0) {
				this.result = false;
			}
		},

		setStartDate: function(date) {
			this.startDate = date;
		},

		setEndDate: function(date) {
			this.endDate = date;
		},

		goModification: function(qnaSno) {
			this.$router.push({name: 'write_qna', query: { qnaSno: qnaSno }});
		},

    initSearchCondition: function() {
      this.questionTitle = "";
      this.answer = "";
    }
	},

  watch : {
		/**
			* 데이터 보여주기
		*/
		qnaList: function() {
			this.showQnaList();
		},

		startDate: function(date) {
      this.startDate = date;
    },

    endDate: function(date) {
      this.endDate = date;
    },

		pageSize: function(pageSize) {
			this.lastPage = 1;
			this.getData(this.questionTitle, this.answer, this.startDate, this.endDate, 1, pageSize);
		},
	},

  /**
	 * 페이지 최초 초기 데이터 생성
	 */
	created() {
    this.startDate = this.getToday();
    this.endDate = this.getToday();
		this.getData(this.questionTitle, this.answer, this.startDate, this.endDate, 1, this.pageSize);
	},
}
</script>
