<template>
  <a-drawer
    title="购物车"
    :maskClosable="false"
    width=850
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <div style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="20" style="width: 100%;margin-top: 20px">
        <a-col style="font-size: 15px;font-weight: 600;color: #000c17;margin-bottom: 15px">收货地址</a-col>
        <a-col :span="24" style="margin-bottom: 30px">
          <a-select style="width: 100%" @change="addressChange">
            <a-select-option :value="item.id" v-for="(item, index) in addressList" :key="index">{{ item.address }}</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="24" style="margin-bottom: 15px" v-if="addressInfo != null">
          <a-descriptions bordered>
            <a-descriptions-item label="省份">
              {{ addressInfo.province }}
            </a-descriptions-item>
            <a-descriptions-item label="城市">
              {{ addressInfo.city }}
            </a-descriptions-item>
            <a-descriptions-item label="区">
              {{ addressInfo.area }}
            </a-descriptions-item>
            <a-descriptions-item label="联系方式">
              {{ addressInfo.phone }}
            </a-descriptions-item>
            <a-descriptions-item label="联系人" :span="2">
              {{ addressInfo.contact }}
            </a-descriptions-item>
            <a-descriptions-item label="详细地址" :span="3">
              {{ addressInfo.address }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        <a-col :span="12" v-for="(item, index) in cartData" :key="index" style="margin-bottom: 15px">
          <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
            <a-card :bordered="false" hoverable>
              <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
                <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
                </div>
              </a-carousel>
              <a-card-meta :title="item.name + ' * '+ item.total + '件'" :description="item.remark.slice(0, 25)+'...'" style="margin-top: 10px"></a-card-meta>
              <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
                <span>{{ item.model }}</span> |
                <span  style="margin-left: 2px">{{ item.model }}</span>
                <span style="color: #f5222d; font-size: 13px;float: right">{{ item.sellPrice }}元</span>
              </div>
            </a-card>
          </div>
        </a-col>
        <a-col v-if="cartData.length === 0">
          <p style="font-size: 23px;font-weight: 500;text-align: center">无数据</p>
          <p style="text-align: center"><a-icon type="folder-open" style="font-size: 50px" theme="twoTone"/></p>
        </a-col>
      </a-row>
    </div>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button v-if="userInfo != null && userInfo.price > 10000" @click="handleSubmit(1)" type="primary" :loading="loading" style="margin-right: .8rem">预付款下单</a-button>
      <a-button @click="handleSubmit(0)" type="primary" :loading="loading">添加到订单</a-button>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
moment.locale('zh-cn')
export default {
  name: 'cartView',
  props: {
    cartShow: {
      type: Boolean,
      default: false
    },
    cartData: {
      type: Array
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.cartShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      userInfo: null,
      addressInfo: null,
      addressList: []
    }
  },
  watch: {
    cartShow: function (value) {
      if (value) {
        // this.selectcommodityDetail()
      }
    }
  },
  mounted () {
    this.selectUserDetail()
  },
  methods: {
    addressChange (value) {
      let addressInfo = this.addressList.filter(e => e.id === value)
      this.addressInfo = addressInfo[0]
    },
    selectUserDetail () {
      this.$get(`/cos/user-info/selectAddressDetail/${this.currentUser.userId}`).then((r) => {
        this.userInfo = r.data.user
        this.addressList = r.data.address
        this.addressInfo = r.data.default
      })
    },
    handleSubmit (type) {
      if (this.cartData.length === 0) {
        this.$message.error('无数据信息！')
        return false
      }
      if (this.addressInfo == null) {
        this.$message.error('请选择收货地址！')
        return false
      }
      let commodityList = []
      this.cartData.forEach(e => {
        commodityList.push({commodityCode: e.code, num: e.total, price: e.sellPrice})
      })
      let values = {userId: this.currentUser.userId, orderDetail: JSON.stringify(commodityList), type, addressId: this.addressInfo.id}
      this.$post('/cos/order-info', values).then((r) => {
        this.$emit('success')
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 150px;
  line-height: 150px;
  overflow: hidden;
}
</style>
