<template>
	<div id="menu">
			<ul class="nav flex-column ms-auto" v-for="menu in menus" v-bind:key="menu.menuId">
				<li class="nav-item" v-if="menu.menuParentId == null">
					<a class="nav-link mt-2 mb-2" style="color:white" @click="showMenu(menu.menuId)">{{ menuName(menu.menuId) }}</a>
					<submenu
					:menu='menu.menuId'
					:children='menu.children'
					:show='show'
					>
					</submenu>
				</li>
			</ul>
		</nav>
	</div>
</template>
<script>
module.exports = {
	components: {
		'submenu': httpVueLoader(contextPath + '/vue/components/common/Submenu.vue'),
	},

	data : function() {
		return {
			menus: [],
			show: ''
		}
	},
	methods: {
		showMenu: function(id) {
			this.show = id;
		},
    menuName: function(id) {
			var message = "message."+id
			return this.$t(message)
		}
	},
	created () {
		axios.get(contextPath + '/menus').then(res => {
			// console.log(res.data.menus);
			this.menus = res.data.menus;
		})
	}
}
</script>

<style scoped>

#menu {
	width: 10%;
	height: 1600px;
	float: left;
	text-align: center;
	font-size: 20px;
	background-color: #ffb8c6;
}

</style>
