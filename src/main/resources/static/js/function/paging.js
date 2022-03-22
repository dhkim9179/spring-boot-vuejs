// 페이징 컴포넌트
Vue.component('paging', {
  template: '\
      <div>\
        <nav>\
          <ul class="pagination justify-content-center">\
            <li class="page-item">\
              <button type="button" class="page-link" @click="setPageData(first, page)"><<</button>\
            </li>\
            <li class="page-item">\
              <button type="button" class="page-link" v-if="lastPage > pageSize"  @click="setPageData(prev_string, page)" :disabled="this.getPrevStatus()"><</button>\
            </li>\
            <li class="page-item" v-for="pageNumber in pages" :key="pageNumber.index">\
              <button type="button" class="page-link" @click="setPageData(move_string, pageNumber)">\
                <span v-if="page == pageNumber"><strong>{{pageNumber}}</strong></span>\
                <span v-else>{{pageNumber}}</span>\
              </button>\
            </li>\
            <li class="page-item">\
              <button type="button" class="page-link" v-if="lastPage > pageSize" @click="setPageData(next_string, page)" :disabled="this.getNextStatus()">></button>\
            </li>\
            <li class="page-item">\
              <button type="button" class="page-link" @click="setPageData(last_string, page)">>></button>\
            </li>\
          </ul>\
        </nav>\
      </div>\
            ',
  props: ['currentpage', 'pagesize', 'lastpage'],
  data() {
    return {
      // 페이징
      page: 1,     // 페이지번호
      pageSize: 10, // row size
      pages: [], // 페이징 정보
      type: '', // 버튼 구분

      // 버튼활성화여부
      prev: '', // 이전페이지
      next: '', // 다음페이지

      lastPage: '',

      first: 'first',
      prev_string: 'prev',
      next_string: 'next',
      last_string: 'last',
      move_string: 'move',
    }
  },
  methods: {
    getPrevStatus: function() {
      if(this.prev == 'disabled') {
        return true;
      }
      return false;
    },

    getNextStatus: function() {
      if(this.next == 'disabled') {
        return true;
      }
      return false;
    },
    /**
     * 페이징 데이터 셋팅
     */
    setPageData: function(type, pageNumber) {
      this.type = type;
      if(type == 'first') {
        this.page = 1;
        this.prev = 'disabled';
        this.next = 'disabled';
        if(this.lastPage > this.pageSize) {
          this.next = 'disabled'
        }
      }
      else if(type == 'prev') {
        this.page = this.page - this.pageSize;
        if(this.page <= 0 || this.page <= this.pageSize) {
          this.page = 1;
          this.prev = 'disabled';
        } else {
          this.page = (this.pageSize * parseInt((this.page / this.pageSize))) + 1;
          this.prev = '';

        }
      }
      else if(type == 'next') {
        if(this.page == this.lastPage) {
          this.page = this.lastPage;
          this.prev = '';
          this.next = 'disabled';
        } else {
          this.page = this.page + this.pageSize;
          if(this.page > this.lastPage ) {
            this.page = (this.pageSize * parseInt((this.lastPage / this.pageSize))) + 1;
            this.prev = '';
            this.next = 'disabled';
          } else {
            this.page = (this.pageSize * parseInt((this.page / this.pageSize))) + 1;
            this.prev = '';
            this.next = '';
          }
        }
      }
      else if(type == 'last') {
        this.page = this.lastPage;
        this.prev = 'disabled';
        this.next = 'disabled';
        if(this.page > this.pageSize) {
          this.prev = '';
        }
      }
      else if(type == 'move') {
        this.prev = '';
        this.next = '';
        this.page = pageNumber;
      }

      // console.log("Set Page: " + this.page);

      // 페이징 데이터
      this.$emit('page-event', this.page);
      // this.getData(this.codeId, this.codeName, this.page);
    },

    /**
     * 페이지 번호 셋팅
     */
    setPages: function () {
      if(this.type != 'move') {
        this.pages = [];
        let startIndex = 1;
        let endIndex = this.pageSize;

        // 첫번째 페이지
        if(this.page <= this.pageSize) {
          if(this.lastPage < this.pageSize) {
            endIndex = this.lastPage;
          } else {
            this.next = '';
          }
        }
        else {
          // 이전 또는 다음페이지
          startIndex = (this.pageSize * parseInt((this.page / this.pageSize))) + 1;
          if(this.lastPage <= (startIndex + 9)) {
            endIndex = this.lastPage;
          } else {
            endIndex = this.pageSize * (parseInt((this.page / this.pageSize))+1)
          }
        }

        for (let index = startIndex; index <= endIndex; index++) {
          this.pages.push(index);
        }
      }
    },
  },

  watch: {
    lastpage: function(lastpage) {
      this.lastPage = lastpage;
      this.setPages();
    },
    currentpage: function(currentpage) {
      this.page = currentpage;
      this.setPages();
    },
    pagesize: function(pagesize) {
      this.pagesize = pagesize;
      this.setPages();
    }
  }
});
