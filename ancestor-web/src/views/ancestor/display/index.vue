<template>
  <el-dialog
    :append-to-body="true"
    :close-on-click-modal="false"
    :visible.sync="dialog"
    :modal="true"
    :lock-scroll="true"
    :fullscreen="true"
    width="100%">
    <div class="container">
      <div class="text-center">
        <TreeChart
          :json="data"
          :class="{landscape: landscape.length}"
          @click-node="clickNode" />
      </div>
    </div>
  </el-dialog>
</template>

<script>
import TreeChart from '@/components/TreeChart/TreeChart'
import { getPersonListDisplay } from '@/api/person'
export default {
  name: 'App',
  components: {
    TreeChart
  },
  data() {
    return {
      dialog: true,
      landscape: [],
      data: {
      },
      horizontal: false,
      collapsable: false,
      expandAll: true,
      labelClassName: 'bg-white'
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    clickNode: function(node) {
      console.log(node)
    },
    init() {
      getPersonListDisplay().then(data => {
        this.data = data
      }).catch(err => {
        console.log(err.response.data.message)
      })
    }
  }
}
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
#app .avat {
  border-radius: 2em;
  border-width: 2px;
}
#app .name {
  font-weight: 700;
}
.foot {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  background: #333;
  padding: 24px;
  overflow: hidden;
  color: #999;
  font-size: 14px;
  text-align: center;
}
.foot a {
  color: #fff;
  margin: 0 0.5em;
}
</style>
