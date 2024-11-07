<template>
    <div>
      <h2>收藏夹</h2>
      <table border="1">
        <thead>
          <tr>
            <th>商品ID</th>
            <th>商品名称</th>
            <th>描述</th>
            <th>价格</th>
            <th>收藏时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(record, index) in favorites" :key="index">
            <td>{{ record.item_id }}</td>
            <td>{{ record.name }}</td>
            <td>{{ record.description }}</td>
            <td>{{ record.price }}</td>
            <td>{{ record.created_at }}</td>
          </tr>
        </tbody>
      </table>
    </div>
</template>
  
<script>
import { getFavorites } from "@/api/user";
  export default {
    name: 'Favorites',
    data() {
      return {
        favorites: []
      };
    },
    mounted: async function() {
        try {
            const response = await getFavorites();  // 等待异步请求完成
            if (response.code === 1) {
              alert("登录已过期，请重新登录！");
              console.log(response.message);
              this.$router.push('/login');
            } else {
              response.data.map((item) => {
                const date = new Date(item.created_at);
                item.created_at = date.toISOString().slice(0, 19).replace('T', ' ');
                item.price = item.price.toFixed(2);
              })
              this.favorites = response.data;         // 赋值给组件的 data 属性
            }
            
        } catch (error) {
            console.error('Error fetching favorites:', error);
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