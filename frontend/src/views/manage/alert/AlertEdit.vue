<template>
  <a-modal v-model="show" title="库存补货" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="8">
          <a-form-item label='商品编号' v-bind="formItemLayout">
            <a-input disabled v-decorator="[
            'code',
            { rules: [{ required: true, message: '请输入商品编号!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='商品名称' v-bind="formItemLayout">
            <a-input disabled v-decorator="[
            'commodityName',
            { rules: [{ required: true, message: '请输入商品名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='当前库存' v-bind="formItemLayout">
            <a-input disabled v-decorator="[
            'currentNum',
            { rules: [{ required: true, message: '请输入当前库存!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='报警信息' v-bind="formItemLayout">
            <a-textarea disabled :rows="4" v-decorator="[
            'alertRemark'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='入库单名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入入库单名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='上传人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'putUser',
            { rules: [{ required: true, message: '请输入上传人!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='入库数量' v-bind="formItemLayout">
            <a-input-number :min="1" style="width: 100%" v-decorator="[
            'putNum',
            { rules: [{ required: true, message: '请输入入库数量!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='入库备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'remark'
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
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
  name: 'alertEdit',
  props: {
    alertEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.alertEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      alertData: null,
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
    setFormValues ({...alert}) {
      this.alertData = alert
      this.rowId = alert.id
      let fields = ['code', 'commodityName', 'alertRemark', 'currentNum']
      let obj = {}
      Object.keys(alert).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(alert['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = alert[key]
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
      this.form.validateFields((err, values) => {
        let storeRecord = [{
          commodityCode: this.alertData.code,
          num: values.putNum,
          type: '1',
          price: this.alertData.purchasePrice
        }]
        if (!err) {
          values.storeRecord = JSON.stringify(storeRecord)
          this.loading = true
          this.$post('/cos/order-put-info/addOrderPut', {
            ...values
          }).then((r) => {
            this.$put('/cos/stock-alert-info', {id : this.rowId, status: 1, putNum: values.putNum}).then((r) => {
              this.reset()
              this.$emit('success')
            })
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
