<template>
    <div>
      <h2>购买记录</h2>
      <table border="1">
        <thead>
          <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>描述</th>
            <th>价格</th>
            <th>购买时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(record, index) in purchaseHistory" :key="index">
            <td>{{ record.item_id }}</td>
            <td>{{ record.item_name }}</td>
            <td>{{ record.description }}</td>
            <td>{{ record.price }}</td>
            <td>{{ record.purchase_date }}</td>
          </tr>
        </tbody>
      </table>
    </div>
</template>
  
<script>
import { getPurchaseHistory } from "@/api/user";
  export default {
    name: 'PurchaseHistory',
    data() {
      return {
        purchaseHistory: []
      };
    },
    mounted: async function() {
        try {
            const response = await getPurchaseHistory();  // 等待异步请求完成
            if (response.code === 1) {
              alert("登录已过期，请重新登录！");
              console.log(response.message);
              this.$router.push('/login');
            } else {
              response.data.map((item) => {
                const date = new Date(item.purchase_date);
                item.purchase_date = date.toISOString().slice(0, 19).replace('T', ' ');
              })
              this.purchaseHistory = response.data;         // 赋值给组件的 data 属性
            }
            
        } catch (error) {
            console.error('Error fetching purchase history:', error);
        }
    }
  };
</script>
  
<style scoped>
  /* 添加一些简单的样式 */
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 8px;
    text-align: left;
    border: 1px solid #ddd;
  }
  
  th {
    background-color: #f4f4f4;
  }
</style>