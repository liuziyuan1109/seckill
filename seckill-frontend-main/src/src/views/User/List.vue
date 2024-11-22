<template>
    <div class="user-list">
      <el-table :data="users" style="width: 100%">
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  export default {
    name: "UserList",
    data() {
      return {
        users: [
          {
            username: "user1",
            name: "张三",
            gender: "男",
            phone: "13812345678",
          },
          {
            username: "user2",
            name: "李四",
            gender: "女",
            phone: "13987654321",
          },
          // more users...
        ],
      };
    },
  };
  </script>
  
  <style>
  .user-list {
    padding: 20px;
  }
  </style>
  