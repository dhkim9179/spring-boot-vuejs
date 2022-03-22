Vue.component('datepicker', {
    template: '\
    <div class="date">\
        <datepicker-child\
              :date="start"\
              @set-date="setStartDate" >\
        </datepicker-child>\
        <span class="txt_wrap">~</span>\
        <datepicker-child\
            :date="end"\
            @set-date="setEndDate" >\
        </datepicker-child>\
    </div>\
    ',
    props: ['startdate','enddate'],
    data: function() {
      return {
        start: '',
        end: '',
      }
    },
    methods: {
     setStartDate: function(date) {
        this.$emit('start-date', date);
      },

      setEndDate: function(date) {
        this.$emit('end-date', date);
      },
    },

    watch: {
 
      startdate: function(date) {      
        this.setStartDate(date);
      },

      enddate: function(date) {
        this.setEndDate(date);     
      },
      
      start: function(date) {      
        this.setStartDate(this.start);
      },

      end: function(date) {
        this.setEndDate(this.end);      
      },

    },
    
    mounted() {
    	this.start = this.getToday();
    	this.end = this.getToday();
    }

  })

  Vue.component('datepicker-child', {
      template: '<input type="text" v-on:input="setValue" :value="datee" maxLength=10 />',
      props: ['date'],
      data: function() {
        return {
          datee: '',
        }
      },

      methods: {
        setValue: function(event) {            
          var inputValue = event.target.value.replaceAll('-',"");              
          if(inputValue.length == 8) {
            if(inputValue.substring(4,6) > 12) {
              inputValue = inputValue.substring(0,4)+"12"+inputValue.substring(6,8);
            }
            if(inputValue.substring(6,8) > 31) {
              inputValue = inputValue.substring(0,6)+"01";
            }

            this.datee = this.getDatetimeFormat(inputValue);
          } else { 
            this.datee = event.target.value;            
          }
        }
      },

      watch: {      
        date: function(date) {                     
          this.datee = this.getDatetimeFormat(date);          
        },             
      },

      mounted: function() {        
        var self = this;
        $(this.$el).datepicker({
            dateFormat: "yy-mm-dd",
            onSelect: function(date) {
              self.$emit('set-date', date)
            }
        }).val(this.datee);
      },      

      beforeDestroy: function() {
        $(this.$el).datepicker('hide').datepicker('destroy')
      }
  });
