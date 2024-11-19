<template>
  <a-modal v-model="show" title="修改商品折扣设置" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" rebate="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='商品名称' v-bind="formItemLayout">
            <a-input disabled v-decorator="[
            'commodityName',
            { rules: [{ required: true, message: '请输入商品名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='商品型号' v-bind="formItemLayout">
            <a-input disabled v-decorator="[
            'model',
            { rules: [{ required: true, message: '请输入商品型号!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='底价折扣' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'lowRate',
            { rules: [{ required: true, message: '请输入底价折扣!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='底价限制' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'lowRestriction',
            { rules: [{ required: true, message: '请输入底价限制!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='特价折扣' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'specialPrice',
            { rules: [{ required: true, message: '请输入特价折扣!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='特价限制' v-bind="formItemLayout">
            <a-input-number style="width: 100%" v-decorator="[
            'specialRestriction',
            { rules: [{ required: true, message: '请输入特价限制!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='正常价格' v-bind="formItemLayout">
            <a-input-number disabled style="width: 100%" v-decorator="[
            'normalPrice',
            { rules: [{ required: true, message: '请输入正常价格!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备注' v-bind="formItemLayout">
            <a-textarea :rows="4" v-decorator="[
            'address'
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'rebateEdit',
  props: {
    rebateEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentrebate: state => state.account.rebate
    }),
    show: {
      get: function () {
        return this.rebateEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...rebate}) {
      this.rowId = rebate.id
      let fields = ['commodityName', 'model', 'lowRate', 'normalPrice', 'specialPrice', 'remark', 'lowRestriction', 'specialRestriction']
      let obj = {}
      Object.keys(rebate).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(rebate['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = rebate[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        if (!err) {
          this.loading = true
          this.$put('/cos/rebate-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
