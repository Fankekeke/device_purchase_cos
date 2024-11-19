<template>
  <a-modal v-model="show" title="订单处理" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="submit" type="primary">
        发货
      </a-button>
      <a-button @click="onClose">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderDetailInfo !== null">
      <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px">
        <a-steps :current="current" progress-dot size="small" v-if="orderDetailInfo.order.type == 0">
          <a-step title="待支付" />
          <a-step title="已支付" />
          <a-step title="已出库" />
          <a-step title="已签收" />
        </a-steps>
        <a-steps :current="current" progress-dot size="small" v-if="orderDetailInfo.order.type == 1">
          <a-step title="待支付" />
          <a-step title="预付款已缴" />
          <a-step title="尾款已缴" />
          <a-step title="已支付" />
          <a-step title="已出库" />
          <a-step title="已签收" />
        </a-steps>
      </div>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span></a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderDetailInfo.order.code }}
        </a-col>
        <a-col :span="8"><b>总价格：</b>
          {{ orderDetailInfo.order.totalPrice ? orderDetailInfo.order.totalPrice : '- -' }} 元
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          <span v-if="orderDetailInfo.order.type == 0">正常订单</span>
          <span v-if="orderDetailInfo.order.type == 1">预付款</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>订单状态：</b>
          <span v-if="orderDetailInfo.order.status == 0">待支付</span>
          <span v-if="orderDetailInfo.order.status == 1">预付款已缴</span>
          <span v-if="orderDetailInfo.order.status == 2">尾款已缴</span>
          <span v-if="orderDetailInfo.order.status == 3">已支付</span>
          <span v-if="orderDetailInfo.order.status == 4">已出库</span>
          <span v-if="orderDetailInfo.order.status == 5">已签收</span>
        </a-col>
        <a-col :span="8"><b>预付款金额：</b>
          {{ orderDetailInfo.order.subsistPrice ? orderDetailInfo.order.subsistPrice : '- -' }} 元
        </a-col>
        <a-col :span="8"><b>欠款金额：</b>
          {{ orderDetailInfo.order.owePrice ? orderDetailInfo.order.owePrice : '- -' }} 元
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>下单时间：</b>
          {{ orderDetailInfo.order.createDate ? orderDetailInfo.order.createDate : '- -' }}
        </a-col>
        <a-col :span="8"><b>支付时间：</b>
          {{ orderDetailInfo.order.payDate ? orderDetailInfo.order.payDate : '- -' }}
        </a-col>
        <a-col :span="8"><b>尾款支付时间：</b>
          {{ orderDetailInfo.order.oweDate ? orderDetailInfo.order.oweDate : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span></a-col>
        <a-col :span="8"><b>用户名称：</b>
          {{ orderDetailInfo.user.name }}
        </a-col>
        <a-col :span="8"><b>医院地址：</b>
          <span v-if="orderDetailInfo.user.type == 1">经销商</span>
          <span v-if="orderDetailInfo.user.type == 2">批发商</span>
          <span v-if="orderDetailInfo.user.type == 3">散客</span>
          <span v-if="orderDetailInfo.user.type == 4">代理商</span>
        </a-col>
        <a-col :span="8"><b>联系人：</b>
          {{ orderDetailInfo.user.contact }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>用户联系方式：</b>
          {{ orderDetailInfo.user.phone }}
        </a-col>
        <a-col :span="8"><b>用户注册时间：</b>
          {{ orderDetailInfo.user.createDate }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">收货地址</span></a-col>
        <a-col :span="8"><b>省份：</b>
          {{ orderDetailInfo.address.province }}
        </a-col>
        <a-col :span="8"><b>城市：</b>
          {{ orderDetailInfo.address.city }}
        </a-col>
        <a-col :span="8"><b>区：</b>
          {{ orderDetailInfo.address.area }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>联系方式：</b>
          {{ orderDetailInfo.address.phone }}
        </a-col>
        <a-col :span="8"><b>联系人：</b>
          {{ orderDetailInfo.address.contact }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>详细地址：</b>
          {{ orderDetailInfo.address.address }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买商品信息</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="orderDetailInfo.detail">
          </a-table>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="50">
        <a-col :span="24">
          <a-divider orientation="left">
            <span style="font-size: 12px;font-family: SimHei">订单发货</span>
          </a-divider>
        </a-col>
        <a-col :span="24">
          <a-form-item label='物流备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-model="auditData.remark"/>
          </a-form-item>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAudit',
  props: {
    orderAuditShow: {
      type: Boolean,
      default: false
    },
    orderAuditData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAuditShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '商品名称',
        dataIndex: 'commodity.name'
      }, {
        title: '型号',
        dataIndex: 'commodity.model'
      }, {
        title: '商品图片',
        dataIndex: 'commodity.images',
        customRender: (text, record, index) => {
          if (!record.commodity.images) return <a-avatar shape="square" icon="user" />
          return <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.commodity.images.split(',')[0] } />
            </template>
            <a-avatar shape="square" icon="user" src={ 'http://127.0.0.1:9527/imagesWeb/' + record.commodity.images.split(',')[0] } />
          </a-popover>
        }
      }, {
        title: '数量',
        dataIndex: 'num',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '单价',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '总金额',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return (row.num * row.price).toFixed(2)
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  watch: {
    'orderAuditShow': function (value) {
      if (value) {
        this.dataInit(this.orderAuditData.id)
        if (this.orderAuditData.type === '0') {
          switch (this.orderAuditData.status) {
            case '0':
              this.current = 0
              break
            case '1':
              this.current = 1
              break
            case '2':
              this.current = 1
              break
            case '3':
              this.current = 1
              break
            case '4':
              this.current = 2
              break
            case '5':
              this.current = 3
              break
          }
        }
        if (this.orderAuditData.type === '1') {
          switch (this.orderAuditData.status) {
            case '0':
              this.current = 0
              break
            case '1':
              this.current = 1
              break
            case '2':
              this.current = 3
              break
            case '3':
              this.current = 3
              break
            case '4':
              this.current = 4
              break
            case '5':
              this.current = 5
              break
          }
        }
      }
    }
  },
  data () {
    return {
      current: 0,
      formItemLayout,
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      auditData: {
        remark: ''
      },
      orderDetailInfo: null
    }
  },
  methods: {
    moment,
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.orderDetailInfo = r.data
      })
    },
    onDateChange (date) {
      this.auditData.reserveDate = moment(date).format('YYYY-MM-DD')
    },
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
    submit () {
      this.$get(`/cos/order-info/ship`, {
        'orderId': this.orderAuditData.id,
        'remark': this.auditData.remark
      }).then((r) => {
        this.cleanData()
        this.$emit('success')
      })
    },
    onClose () {
      this.cleanData()
      this.$emit('close')
    },
    cleanData () {
      this.auditData.remark = ''
    }
  }
}
</script>

<style scoped>

</style>
