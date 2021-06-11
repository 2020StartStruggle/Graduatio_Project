<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
      <el-form-item v-if="false" label="父编号">
        <el-input v-model="parent.id" style="width: 370px;"/>
      </el-form-item>
      <el-form-item v-if="isAdd" label="父亲姓名">
        <el-input v-model="parent.name" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.name" autofocus="true" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="妻子">
        <el-input v-model="form.wife" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="居住地">
        <el-select v-model="form.liveId" clearable placeholder="" class="filter-item" style="width: 130px">
          <el-option v-for="item in liveList" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="准确">
        <el-switch v-model="form.isSure" active-value="1" inactive-value="0"/>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="生于">
        <el-date-picker v-model="form.born" type="date" style="width: 370px;"/>
      </el-form-item>
      <el-form-item label="卒于">
        <el-date-picker v-model="form.dead" type="date" style="width: 370px;"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/person'
import { queryAll } from '@/api/live'
export default {
  props: {
    parent: {
      type: Object,
      default: function() {
        return {
          id: '',
          name: '',
          wife: '',
          parentId: '',
          levelId: '',
          liveId: '',
          isSure: '',
          remark: '',
          born: '',
          dead: ''
        }
      }
    },
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
      liveList: [],
      form: {
        id: '',
        name: '',
        wife: '',
        parentId: '',
        levelId: '',
        liveId: '',
        isSure: '1',
        remark: '',
        born: '',
        dead: ''
      },
      rules: {}
    }
  },
  watch: {
    isAdd(val, oldVal) {
      if (val) {
        this.form.parentId = this.parent.id
        this.form.liveId = this.parent.liveId
        this.form.levelId = this.parent.levelId + 1
        this.form.isSure = '1'
      }
    }
  },
  mounted() {
    queryAll({}).then(res => {
      this.liveList = res.content
    }).catch(err => {
      console.log(err.response.data.message)
    })
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.loading = true
      if (this.isAdd) {
        this.doAdd()
      } else this.doEdit()
    },
    doAdd() {
      add(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '添加成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.getPersonList()
      }).catch(err => {
        this.loading = false
        console.log(err.response.data.message)
      })
    },
    doEdit() {
      edit(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '修改成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.getPersonList()
      }).catch(err => {
        this.loading = false
        console.log(err.response.data.message)
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        name: '',
        wife: '',
        parentId: '',
        levelId: '',
        liveId: '',
        isSure: '',
        remark: '',
        born: '',
        dead: ''
      }
    }
  }
}
</script>

<style scoped>

</style>
