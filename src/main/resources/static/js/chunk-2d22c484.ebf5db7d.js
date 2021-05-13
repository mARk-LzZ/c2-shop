(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d22c484"],{f323:function(e,t,r){"use strict";r.r(t);var s=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("el-breadcrumb",{attrs:{"separator-class":"el-icon-arrow-right"}},[r("el-breadcrumb-item",{attrs:{to:{path:"/home"}}},[e._v("首页")]),r("el-breadcrumb-item",[e._v("用户管理")]),r("el-breadcrumb-item",[e._v("用户列表")])],1),r("el-card",[r("el-row",{attrs:{gutter:20}},[r("el-col",{attrs:{span:7}},[r("el-input",{attrs:{placeholder:"请输入内容",clearable:""},on:{clear:e.getUserList},model:{value:e.queryInfo.query,callback:function(t){e.$set(e.queryInfo,"query",t)},expression:"queryInfo.query"}},[r("el-button",{attrs:{slot:"append",icon:"el-icon-search"},on:{click:e.getUserList},slot:"append"})],1)],1),r("el-col",{attrs:{span:4}},[r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dialogVisible=!0}}},[e._v("添加用户")])],1)],1),r("el-table",{staticStyle:{width:"100%"},attrs:{data:e.userlist,border:"",stripe:""}},[r("el-table-column",{attrs:{type:"index"}}),r("el-table-column",{attrs:{prop:"username",label:"姓名"}}),r("el-table-column",{attrs:{prop:"email",label:"邮箱"}}),r("el-table-column",{attrs:{prop:"mobilephone",label:"电话"}}),r("el-table-column",{attrs:{prop:"roleid",label:"角色"}}),r("el-table-column",{attrs:{label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-switch",{on:{change:function(r){return e.userStateChanged(t.row)}},model:{value:t.row.mg_state,callback:function(r){e.$set(t.row,"mg_state",r)},expression:"scope.row.mg_state"}})]}}])}),r("el-table-column",{attrs:{label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{type:"primary",icon:"el-icon-edit",size:"mini"},on:{click:function(r){return e.showEditDialog(t.row.id)}}}),r("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"mini"},on:{click:function(r){return e.deleteUser(t.row.id)}}}),r("el-tooltip",{attrs:{enterable:!1,effect:"dark",content:"分配角色",placement:"top-start"}},[r("el-button",{attrs:{type:"warning",icon:"el-icon-setting",size:"mini"},on:{click:function(r){return e.setRole(t.row)}}})],1)]}}])})],1),r("el-pagination",{attrs:{"current-page":e.queryInfo.pagenum,"page-sizes":[1,2,5,10],"page-size":e.queryInfo.pagesize,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),r("el-dialog",{attrs:{title:"添加用户",visible:e.dialogVisible,width:"50%"},on:{"update:visible":function(t){e.dialogVisible=t},close:e.addDialogClosed}},[r("el-form",{ref:"addUserFormRef",staticClass:"demo-ruleForm",attrs:{model:e.addUserForm,rules:e.addUserFormRules,"label-width":"70px"}},[r("el-form-item",{attrs:{label:"用户名",prop:"username"}},[r("el-input",{model:{value:e.addUserForm.username,callback:function(t){e.$set(e.addUserForm,"username",t)},expression:"addUserForm.username"}})],1),r("el-form-item",{attrs:{label:"密码",prop:"password"}},[r("el-input",{model:{value:e.addUserForm.password,callback:function(t){e.$set(e.addUserForm,"password",t)},expression:"addUserForm.password"}})],1),r("el-form-item",{attrs:{label:"邮箱",prop:"email"}},[r("el-input",{model:{value:e.addUserForm.email,callback:function(t){e.$set(e.addUserForm,"email",t)},expression:"addUserForm.email"}})],1),r("el-form-item",{attrs:{label:"手机",prop:"mobile"}},[r("el-input",{model:{value:e.addUserForm.mobile,callback:function(t){e.$set(e.addUserForm,"mobile",t)},expression:"addUserForm.mobile"}})],1)],1),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.addUser}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"修改用户",visible:e.editDialogVisible,width:"50%"},on:{"update:visible":function(t){e.editDialogVisible=t},close:e.editDialogClosed}},[r("el-form",{ref:"editUserFormRef",staticClass:"demo-ruleForm",attrs:{model:e.editUserForm,rules:e.editUserFormRules,"label-width":"70px"}},[r("el-form-item",{attrs:{label:"用户名"}},[r("el-input",{attrs:{disabled:""},model:{value:e.editUserForm.username,callback:function(t){e.$set(e.editUserForm,"username",t)},expression:"editUserForm.username"}})],1),r("el-form-item",{attrs:{label:"邮箱",prop:"email"}},[r("el-input",{model:{value:e.editUserForm.email,callback:function(t){e.$set(e.editUserForm,"email",t)},expression:"editUserForm.email"}})],1),r("el-form-item",{attrs:{label:"手机",prop:"mobile"}},[r("el-input",{model:{value:e.editUserForm.mobile,callback:function(t){e.$set(e.editUserForm,"mobile",t)},expression:"editUserForm.mobile"}})],1)],1),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.editDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.editUser}},[e._v("确 定")])],1)],1),r("el-dialog",{attrs:{title:"分配角色",visible:e.setRoleDialogVisible,width:"50%"},on:{"update:visible":function(t){e.setRoleDialogVisible=t},close:e.setRoleDialogClosed}},[r("div",[r("p",[e._v("当前的用户:"+e._s(e.userInfo.username))]),r("p",[e._v("当前的角色:"+e._s(e.userInfo.roleid))]),r("p",[e._v("分配新角色: "),r("el-select",{attrs:{placeholder:"请选择新角色"},model:{value:e.selectRoleId,callback:function(t){e.selectRoleId=t},expression:"selectRoleId"}},e._l(e.rolesList,(function(e){return r("el-option",{key:e.id,attrs:{label:e,value:e}})})),1)],1)]),r("div",{attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.setRoleDialogVisible=!1}}},[e._v("取 消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.saveRoleInfo}},[e._v("确 定")])],1)])],1)},i=[],a=r("1da1"),l=r("5530"),o=(r("96cf"),r("2f62")),n={data:function(){var e=function(e,t,r){var s=/^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/;if(s.test(t))return r();r(new Error("请输入合法的邮箱"))},t=function(e,t,r){var s=/^[1][3,4,5,7,8][0-9]{9}$/;if(s.test(t))return r();r(new Error("请输入合法的手机号"))};return{queryInfo:{query:"",pagenum:1,pagesize:5},userlist:[{username:1,email:2,mobile:1,role_name:"管理员"}],total:10,dialogVisible:!1,editDialogVisible:!1,addUserForm:{username:"",password:"",email:"",mobile:""},setRoleDialogVisible:!1,addUserFormRules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"},{require:!0,min:3,max:10,message:"用户名长度在 3 到 10 个字符",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"},{require:!0,min:6,max:15,message:"密码长度在 6 到 15 个字符",trigger:"blur"}],email:[{required:!0,message:"请输入邮箱",trigger:"blur"},{validator:e,trigger:"blur"}],mobile:[{required:!0,message:"请输入手机",trigger:"blur"},{validator:t,trigger:"blur"}]},editUserForm:{},editUserFormRules:{email:[{required:!0,message:"请输入邮箱",trigger:"blur"},{validator:e,trigger:"blur"}],mobile:[{required:!0,message:"请输入手机",trigger:"blur"},{validator:t,trigger:"blur"}]},userInfo:"",rolesList:[1,2,3],selectRoleId:"",roleid:""}},methods:Object(l["a"])(Object(l["a"])({},Object(o["b"])(["setAdmin","setAdmin2"])),{},{getUserList:function(){var e=this;this.$axios.get("http://8.141.56.170:9100/admin/userlist/"+this.roleid+"/1",{params:{limit:this.queryInfo.pagesize,page:this.queryInfo.pagenum}}).then((function(t){console.log(t);for(var r=t.data.data,s=0;s<r.length;s++)r[s].mg_state=!r[s].mg_state;0!=r.length?(e.userlist=r,e.total=e.userlist.length):e.$axios.get("http://8.141.56.170:9100/admin/userlist/1/1",{params:{limit:e.queryInfo.pagesize,page:e.queryInfo.pagenum}}).then((function(t){for(var r=t.data.data,s=0;s<r.length;s++)r[s].mg_state=!r[s].mg_state;e.userlist=r}))}))},handleSizeChange:function(e){this.queryInfo.pagesize=e,this.getUserList()},handleCurrentChange:function(e){this.queryInfo.pagenum=e,this.getUserList()},userStateChanged:function(e){var t;t=1==e.mg_state?1:0,this.$axios.put("http://8.141.56.170:9100/admin/user/forbid/"+e.userid+"/"+t).then((function(e){console.log(e)}))},addDialogClosed:function(){this.$refs.addUserFormRef.resetFields()},showEditDialog:function(){this.editDialogVisible=!0},editDialogClosed:function(){this.$refs.editUserFormRef.resetFields()},setRole:function(e){var t=this;return Object(a["a"])(regeneratorRuntime.mark((function r(){return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:t.$axios({method:"post",url:"http://8.141.56.170:9100/admin/finduserbyname",data:{username:e.username}}).then((function(e){console.log(e),t.userInfo=e.data.data[0]})),t.setRoleDialogVisible=!0;case 2:case"end":return r.stop()}}),r)})))()},saveRoleInfo:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(e.selectRoleId){t.next=2;break}return t.abrupt("return",e.$message.error("请选择要分配的角色！"));case 2:e.$axios.put("http://8.141.56.170:9100/admin/set/administrator/"+e.userInfo.userid+"/"+e.selectRoleId).then((function(t){console.log(t),200==t.data.status?(e.$message.success("分配角色成功！"),e.getUserList(),e.setRoleDialogVisible=!1):e.$alert(t.data.message)}));case 3:case"end":return t.stop()}}),t)})))()},setRoleDialogClosed:function(){this.selectRoleId="",this.userInfo={}}}),created:function(){this.$store.state.user.isadmin&&(this.roleid=2),this.$store.state.user.isadmin2&&(this.roleid=1),this.getUserList()}},u=n,d=r("2877"),m=Object(d["a"])(u,s,i,!1,null,null,null);t["default"]=m.exports}}]);
//# sourceMappingURL=chunk-2d22c484.ebf5db7d.js.map