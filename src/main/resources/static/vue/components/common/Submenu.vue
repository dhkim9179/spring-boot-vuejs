<template>
	<div>
		<ul class="nav flex-column ms-auto" v-show="showMenu()">
			<li class="nav-item" v-for="child in children" v-bind:key="child.menuId">
				<a class="nav-link mt-1 mb-1"><router-link style="color:black" v-bind:to="child.menuUrl">{{ menuName(child.menuId) }}</router-link></a>
			</li>
		</ul>
	</div>
</template>

<script>
module.exports = {
	name: 'submenu',
	props: ['menu', 'children', 'show'],
	methods: {
		showMenu: function() {
			if(this.menu == this.show) {
				return true;
			}
			return false;
		},
    menuName: function(id) {
			var message = "message."+id
			return this.$t(message)
		},
	},
}
</script>
