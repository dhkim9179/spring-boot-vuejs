Vue.mixin({
    methods: {
		// 당일
		 getToday: function() {
		    const date = new Date();
		    let month = date.getMonth()+1;
		    let today = date.getDate();
		    if((month != '10') && (month != '11') && (month != '12')) {
		      month = "0" + month;
		    } 
		    
		    if (today.length == 1) {
		    	today = "0" + today;
		    }
		    let result = (date.getFullYear() + "-" + month + "-" + today);
		    return result;
		  },
		  
		  getDatetimeFormat: function(date) {
	        if(date == null) {
	          return date;
	        }
	
	        if(date.length != 8 && date.length != 14) {
	          return date;
	        }
	
	        if(date.length == 8) {
	          return (date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8));
	        }
	
	        else if(date.length == 14) {
	          return (date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6,8) + " " + date.substring(8,10) + ":" + date.substring(10,12) + ":" + date.substring(12,14));
	        }
	
	        return date;
	      },
	},
})
		 