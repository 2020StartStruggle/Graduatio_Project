<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="filterText" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" />
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
          v-permission="['admin','person:add']"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="addSon">添加儿子
        </el-button>
        <el-button
          v-permission="['admin','person:add']"
          class="filter-item"
          size="mini"
          type="success"
          icon="el-icon-plus"
          @click="addBrother">添加兄弟
        </el-button>
        <el-button
          v-permission="['admin','person:edit']"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-edit"
          @click="edit">编辑信息
        </el-button>
        <el-button
          v-permission="['admin','person:del']"
          class="filter-item"
          size="mini"
          type="danger"
          icon="el-icon-delete"
          @click="edit">删除
        </el-button>
      </div>
      <!-- 导出 -->
      <div style="display: inline-block;">
        <el-button
          :loading="downloadLoading"
          size="mini"
          class="filter-item"
          type="warning"
          icon="el-icon-download"
          @click="download">导出
        </el-button>
      </div>
    </div>
    <!--表单组件-->
    <eForm ref="form" :parent="parent" :is-add="isAdd"/>
    <div v-loading.body="loading" class="tree">
      <el-tree
        v-loading="loading"
        ref="tree"
        :data="personList"
        :props="defaultProps"
        :expand-on-click-node="false"
        :highlight-current="true"
        :filter-node-method="filterNode"
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        empty-text="数据加载中..."
        node-key="id"
        default-expand-all
        draggable
        @node-click="selectPerson"
        @node-drag-start="handleDragStart"
        @node-drag-enter="handleDragEnter"
        @node-drag-leave="handleDragLeave"
        @node-drag-over="handleDragOver"
        @node-drag-end="handleDragEnd"
        @node-drop="handleDrop"
      />
    </div>
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/initData'
import { get, del, downloadPerson, getPersonListTree } from '@/api/person'
import { downloadFile } from '@/utils/index'
import eForm from './form'
export default {
  components: { eForm },
  mixins: [initData],
  data() {
    return {
      filterText: '',
      loading: true,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      selPerson: null,
      delLoading: false,
      personList: [],
      queryTypeOptions: [
        { key: 'name', display_name: '姓名' }
      ],
      parent: {}
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getPersonList()
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    checkPermission,
    beforeInit() {
      this.url = 'api/person'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort }
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) {
        this.params[type] = value
      }
      return true
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    getPersonList() {
      getPersonListTree().then(res => {
        this.personList = res
        this.loading = false
      })
    },
    subDelete(id) {
      if (!this.selPerson) {
        this.$notify({
          title: '请选择节点后操作！',
          type: 'warning',
          duration: 2500
        })
        return
      }
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
        console.log(err.response.data.message)
      })
    },
    addSon() {
      if (!this.selPerson) {
        this.$notify({
          title: '请选择节点后操作！',
          type: 'warning',
          duration: 2500
        })
        return
      }
      get(this.selPerson.id).then(data => {
        this.parent = data
        this.isAdd = true
        this.$refs.form.dialog = true
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },
    addBrother() {
      if (!this.selPerson) {
        this.$notify({
          title: '请选择节点后操作！',
          type: 'warning',
          duration: 2500
        })
        return
      }
      get(this.selPerson.parentId).then(data => {
        this.parent = data
        this.isAdd = true
        this.$refs.form.dialog = true
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },
    edit(data) {
      if (!this.selPerson) {
        this.$notify({
          title: '请选择节点后操作！',
          type: 'warning',
          duration: 2500
        })
        return
      }
      get(this.selPerson.id).then(data => {
        this.isAdd = false
        const _this = this.$refs.form
        _this.form = data
        _this.dialog = true
      }).catch(err => {
        console.log(err.response.data.message)
      })
    },
    selectPerson(person, node, arr) {
      this.selPerson = person
    },
    // 导出
    download() {
      this.beforeInit()
      this.downloadLoading = true
      downloadPerson(this.params).then(result => {
        downloadFile(result, 'Person列表', 'xlsx')
        this.downloadLoading = false
      }).catch(() => {
        this.downloadLoading = false
      })
    },
    handleDragStart(node, ev) {
      console.log('drag start', node)
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log('tree drag enter: ', dropNode.label)
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log('tree drag leave: ', dropNode.label)
    },
    handleDragOver(draggingNode, dropNode, ev) {
      console.log('tree drag over: ', dropNode.label)
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log('tree drag end: ', dropNode && dropNode.label, dropType)
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log('tree drop: ', dropNode.label, dropType)
    },
    allowDrop(draggingNode, dropNode, type) {
      if (dropNode.data.parentId === draggingNode.data.parentId) {
        return true
      } else {
        return false
      }
    },
    allowDrag(draggingNode) {
      return true
    }
  }
}
</script>

<style scoped>
.tree {
    overflow-y:auto;
    overflow-x: auto;
    height: 700px;
}
</style>
