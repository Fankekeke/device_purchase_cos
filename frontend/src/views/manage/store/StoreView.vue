<template>
  <a-modal v-model="show" title="商品出入库记录" @cancel="onClose" :width="1000">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="storehouseData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>商品名称：</b>
          {{ storehouseData.name !== null ? storehouseData.name : '- -' }}
        </a-col>
        <a-col :span="8"><b>商品型号：</b>
          {{ storehouseData.model !== null ? storehouseData.model : '- -' }}
        </a-col>
        <a-col :span="8"><b>单位：</b>
          {{ storehouseData.unit !== null ? storehouseData.unit : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前库存：</b>
          {{ storehouseData.num !== null ? storehouseData.num : '- -' }}
        </a-col>
        <a-col :span="8"><b>商品类型：</b>
          {{ storehouseData.typeName !== null ? storehouseData.typeName : '- -' }}
        </a-col>
        <a-col :span="8"><b>采购价（元）：</b>
          {{ storehouseData.purchasePrice !== null ? storehouseData.purchasePrice : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商品图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange">
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">出入库记录</span></a-col>
        <a-col :span="24">
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="出库">
              <a-table :columns="columns" :data-source="outData">
              </a-table>
            </a-tab-pane>
            <a-tab-pane key="2" tab="入库">
              <a-table :columns="columns" :data-source="putData">
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-col>
      </a-row>
      <br/>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: 'storehouseView',
  props: {
    storehouseShow: {
      type: Boolean,
      default: false
    },
    storehouseData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.storehouseShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '单号',
        dataIndex: 'orderNumber'
      }, {
        title: '数量',
        dataIndex: 'num'
      }, {
        title: '单价',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          if (text !== null) {
            return '￥' + text.toFixed(2)
          } else {
            return '- -'
          }
        }
      }, {
        title: '总价格',
        dataIndex: 'totalPrice',
        customRender: (text, row, index) => {
          if (text !== null) {
            return '￥' + text.toFixed(2)
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      loading: false,
      putData: [],
      outData: [],
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    storehouseShow: function (value) {
      if (value) {
        this.getOutInDetail(this.storehouseData.code)
        this.imagesInit(this.storehouseData.images)
      }
    }
  },
  methods: {
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
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
    getOutInDetail (commodityCode) {
      this.$get(`/cos/store-record-info/selectOutInDetail`, { commodityCode }).then((r) => {
        this.putData = r.data.putData
        this.outData = r.data.outData
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
</style>
