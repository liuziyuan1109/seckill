<template>
  <div class="order-list">
    <h2>我的订单</h2>
    <table>
      <thead>
        <tr>
          <th>订单ID</th>
          <th>商品名称</th>
          <th>商品数量</th>
          <th>商品价格</th>
          <th>订单状态</th>
          <th>下单时间</th>
          <th>支付时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.goodsName }}</td>
          <td>{{ order.goodsCount }}</td>
          <td>{{ order.goodsPrice }}</td>
          <td>{{ getOrderStatus(order.status) }}</td>
          <td>{{ order.createDate }}</td>
          <td>{{ order.payDate || '未支付' }}</td>
          <td>
            <el-button v-if="order.status === 0" type="danger" size="small"
              @click="confirmCancelOrder(order.id)">
              取消订单
            </el-button>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button :disabled="currentPage != 2" @click="fetchOrders(currentPage - 1)">上一页</button>
      <span>当前页：{{ currentPage }}</span>
      <button :disabled="currentPage === totalPages" @click="fetchOrders(currentPage + 1)">下一页</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const API_BASE_URL = process.env.VUE_APP_API_BASE_URL; // 后端服务地址

export default {
  data() {
    return {
      orders: [], // 订单数据
      currentPage: 1, // 当前页
      totalPages: 0, // 总页数
      pageSize: 10, // 每页条数
    };
  },
  methods: {
    // 提示确认取消订单
    confirmCancelOrder(orderId) {
      this.$confirm('您确定要取消此订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.cancelOrder(orderId);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作',
          });
        });
    },

    // 调用后端接口取消订单
    async cancelOrder(orderId) {
      try {
        const response = await axios.post(`${API_BASE_URL}/api/orders/cancel`, { orderId });
        if (response.code != 200) {
          throw new Error('取消订单失败');
        }
        this.$message({
          type: 'success',
          message: '订单已取消',
        });
        // 更新订单列表
        this.fetchOrders(this.currentPage);
      } catch (error) {
        console.error(error);
        this.$message.error('取消订单时发生错误');
      }
    },
    // 获取订单列表
    fetchOrders(page) {
      axios
        .get(`${API_BASE_URL}/api/orders/list`, {
          params: {
            userId: localStorage.getItem('userid'), // 获取用户 ID 
            page: page - 1, // 后端分页从 0 开始
            size: this.pageSize,
          },
        })
        .then((response) => {
          this.orders = response.content;
          this.totalPages = response.totalPages;
          this.currentPage = page;
        })
        .catch((error) => {
          console.error("获取订单失败：", error);
          this.$message.error('加载订单时发生错误');
        });
    },
    // 查看订单详情
    viewOrder(orderId) {
      this.$router.push(`/orders/${orderId}`);
    },
    // 解析订单状态
    getOrderStatus(status) {
      switch (status) {
        case 0:
          return "未支付";
        case 1:
          return "已支付";
        case 2:
          return "已发货";
        case 3:
          return "已取消";
        default:
          return "未知状态";
      }
    },
  },
  mounted() {
    this.fetchOrders(this.currentPage);
  },
};
</script>


<style scoped>
.order-list {
  width: 80%;
  margin: 20px auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

button {
  padding: 5px 10px;
  margin: 5px;
  cursor: pointer;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
