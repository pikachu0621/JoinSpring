(function(){"use strict";var e={426:function(e,t,a){var s=a(6369),r=a(8499),i=a.n(r),o=a(9252),l=function(){var e=this,t=e._self._c;return t("div",{attrs:{id:"#app"}},[e.isShowLogin?t("MyLogin"):t("MyFrame")],1)},n=[],u=function(){var e=this,t=e._self._c;return t("div",{staticClass:"app"},[t("el-menu",{staticClass:"el-menu-vertical",attrs:{"background-color":"#333744","text-color":"#fff","active-text-color":"#409Eff","default-active":e.selectIndex,collapse:e.isCollapse},on:{open:e.handleOpen,close:e.handleClose}},[t("div",{staticClass:"logo-div el-menu-item"},[t("img",{staticClass:"logo",attrs:{src:a(4759),alt:"签到系统用户管理"}}),t("span",{staticStyle:{"font-weight":"bold"},attrs:{slot:"title"},slot:"title"},[e._v("签到系统用户管理")])]),t("el-menu-item",{staticStyle:{"margin-top":"2vh"},attrs:{index:"1"},on:{click:function(t){e.isShow="MyMain"}}},[t("i",{staticClass:"el-icon-s-home"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("首页")])]),t("el-menu-item",{attrs:{index:"2"},on:{click:function(t){e.isShow="UserManagement"}}},[t("i",{staticClass:"el-icon-s-custom"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("用户管理")])]),t("el-menu-item",{attrs:{index:"6"},on:{click:function(t){e.isShow="GroupManagement"}}},[t("i",{staticClass:"el-icon-menu"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("群组管理")])]),t("el-menu-item",{attrs:{index:"3"}},[t("i",{staticClass:"el-icon-s-flag"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("签到管理")])]),t("el-menu-item",{attrs:{index:"4"}},[t("i",{staticClass:"el-icon-s-platform"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("前端设置")])]),t("el-menu-item",{attrs:{index:"5"}},[t("i",{staticClass:"el-icon-s-help"}),t("span",{attrs:{slot:"title"},slot:"title"},[e._v("管理员")])])],1),t("div",{staticClass:"main-container",style:"margin-left:"+e.mainContainerLeft+"px"},[t("MyNavbar"),t("div",{staticClass:"cps-main-container"},[t("transition",{attrs:{name:"fade"}},[t("MyMain",{directives:[{name:"show",rawName:"v-show",value:"MyMain"===e.isShow,expression:"isShow === 'MyMain'"}],staticClass:"chc"})],1),t("transition",{attrs:{name:"fade"}},[t("UserManagement",{directives:[{name:"show",rawName:"v-show",value:"UserManagement"===e.isShow,expression:"isShow === 'UserManagement'"}],staticClass:"chc"})],1),t("transition",{attrs:{name:"fade"}},[t("GroupManagement",{directives:[{name:"show",rawName:"v-show",value:"GroupManagement"===e.isShow,expression:"isShow === 'GroupManagement'"}],staticClass:"chc"})],1)],1)],1)],1)},c=[],d=function(){var e=this,t=e._self._c;return t("div",[t("el-row",{attrs:{gutter:20}},[t("el-col",{attrs:{span:12}},[t("div",{staticClass:"grid-content bg-purple"},[t("el-card",{staticClass:"box-card"},[t("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"main"}})])],1)]),t("el-col",{attrs:{span:12}},[t("div",{staticClass:"grid-content bg-purple-light"},[t("el-card",{staticClass:"box-card"},[t("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"main1"}})])],1)])],1),t("el-row",{attrs:{gutter:20}},[t("el-col",{attrs:{span:12}},[t("div",{staticClass:"grid-content bg-purple"},[t("el-card",{staticClass:"box-card"},[t("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"main2"}})])],1)]),t("el-col",{attrs:{span:12}},[t("div",{staticClass:"grid-content bg-purple-light"},[t("el-card",{staticClass:"box-card"},[t("div",{staticStyle:{width:"100%",height:"400px"},attrs:{id:"main3"}})])],1)])],1)],1)},m=[];function h(e,t,a){const s=t.init(document.getElementById(e),"dark");return a.backgroundColor={type:"radial",x:.3,y:.3,r:.8,colorStops:[{offset:0,color:"#424859"},{offset:1,color:"#424859"}]},s.setOption(a),s}function p(...e){for(let t=0;t<e.length;t++){const a=e[t];if(null===a||void 0===a)return!0;if(f(a)&&a.length<=0)return console.log(55555),!0}return!1}function f(e){return"[object String]"===Object.prototype.toString.call(e)}var g={name:"MyMain",data(){return{initEcharts:{m1:void 0,m2:void 0,m3:void 0,m4:void 0}}},methods:{drawChart(){void 0!==this.initEcharts.m1?this.initEcharts.m1.resize():this.initEcharts.m1=h("main",this.$echarts,{title:{text:"软件每日启动次数"},tooltip:{trigger:"axis"},xAxis:{type:"category",data:["03-10","03-11","03-12","03-13","03-14","03-15","03-16"]},yAxis:{type:"value"},series:[{data:[230,330,332,410,435,247,600],type:"line",markLine:{data:[{type:"average",name:""}]}}]}),void 0!==this.initEcharts.m2?this.initEcharts.m2.resize():this.initEcharts.m2=h("main1",this.$echarts,{backgroundColor:"#2c343c",title:{text:"软件用户画像"},tooltip:{trigger:"item"},visualMap:{show:!1,min:80,max:600,inRange:{colorLightness:[0,1]}},series:[{name:"Access From",type:"pie",radius:"55%",center:["50%","50%"],data:[{value:335,name:"男"},{value:310,name:"女"},{value:274,name:"河南"},{value:235,name:"老师"}].sort((function(e,t){return e.value-t.value})),roseType:"radius",label:{color:"rgba(255, 255, 255, 0.3)"},labelLine:{lineStyle:{color:"rgba(255, 255, 255, 0.3)"},smooth:.2,length:10,length2:20},itemStyle:{color:"#499a5c",shadowBlur:200,shadowColor:"rgba(0, 0, 0, 0.5)"},animationType:"scale",animationEasing:"elasticOut",animationDelay:function(){return 200*Math.random()}}]}),void 0!==this.initEcharts.m3?this.initEcharts.m3.resize():this.initEcharts.m3=h("main2",this.$echarts,{title:{text:"软件崩溃次数"},tooltip:{trigger:"axis"},xAxis:{type:"category",data:["03-10","03-11","03-12","03-13","03-14","03-15","03-16"]},yAxis:{type:"value"},series:[{areaStyle:{},data:[20,10,12,10,5,6,10],type:"line",color:"rgba(255,0,0,0.6)",markPoint:{data:[{type:"max",name:"Max"},{type:"min",name:"Min"}]},markLine:{data:[{type:"average",name:""}]}}]}),void 0!==this.initEcharts.m4?this.initEcharts.m4.resize():this.initEcharts.m4=h("main3",this.$echarts,{title:{text:"软件新增用户"},tooltip:{trigger:"axis"},xAxis:{data:["03-10","03-11","03-12","03-13","03-14","03-15","03-16"]},yAxis:{},series:[{name:"软件新增用户",type:"bar",data:[100,150,136,110,140,180,166],markLine:{data:[{type:"average",name:""}]}}]})}},mounted(){this.drawChart(),window.onresize=()=>(()=>{this.drawChart()})()}},b=g,v=a(1001),w=(0,v.Z)(b,d,m,!1,null,"4d31aff0",null),y=w.exports,D=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{"margin-top":"10px"}}),t("el-button",{attrs:{type:"primary",size:"small",icon:"el-icon-plus"}},[e._v("新增")]),t("el-button",{attrs:{type:"success",size:"small",icon:"el-icon-refresh-left"},on:{click:function(t){return e.refreshData(!0)}}},[e._v("刷新")]),t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData}},[t("el-table-column",{attrs:{width:"40%",align:"center",label:"ID",prop:"id"}}),t("el-table-column",{attrs:{label:"用户账号",align:"center",prop:"userAccount"}}),t("el-table-column",{attrs:{label:"用户头像",align:"center",prop:"userImg"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-image",{staticStyle:{width:"26px",height:"26px","border-radius":"13px"},attrs:{src:e.baseAddress+a.row.userImg+"&token="+e.token,"preview-src-list":e.previewSrcList}})]}}])}),t("el-table-column",{attrs:{align:"center",label:"用户性别"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-tag",{attrs:{effect:"light",size:"small",color:a.row.userSex?"#3a78bf":"#b23abf"}},[t("span",{staticStyle:{color:"white"}},[e._v(e._s(a.row.userSex?"男":"女"))])])]}}])}),t("el-table-column",{attrs:{align:"center",label:"用户姓名",prop:"userName"}}),t("el-table-column",{attrs:{align:"center",label:"用户学校",prop:"userUnit"}}),t("el-table-column",{attrs:{label:"用户日期",align:"center",prop:"userBirth"}}),t("el-table-column",{attrs:{label:"用户年龄",align:"center",prop:"userAge"}}),t("el-table-column",{attrs:{label:"用户简介",width:"100%","show-overflow-tooltip":!0,align:"center",prop:"userIntroduce"}}),t("el-table-column",{attrs:{label:"用户等级",width:"100%","show-overflow-tooltip":!0,align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-tag",{attrs:{effect:"light",size:"small",color:2===a.row.userGrade?"#bf763a":1===a.row.userGrade?"#60bf3a":"#3abfb5"}},[t("span",{staticStyle:{color:"white"}},[e._v(e._s(2===a.row.userGrade?"ROOT":1===a.row.userGrade?"管理员":"普通用户"))])])]}}])}),t("el-table-column",{attrs:{width:"100%","show-overflow-tooltip":!0,align:"center",label:"用户拉黑"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-switch",{attrs:{"active-color":"#ff4949","inactive-color":"#13ce66"},on:{change:function(t){return e.userLimit(a.row.id,a.row.userLimit)}},model:{value:a.row.userLimit,callback:function(t){e.$set(a.row,"userLimit",t)},expression:"scope.row.userLimit"}})]}}])}),t("el-table-column",{attrs:{width:"200px","show-overflow-tooltip":!0,align:"right"},scopedSlots:e._u([{key:"header",fn:function({}){return[t("el-input",{staticStyle:{color:"white"},attrs:{size:"mini",placeholder:"输入关键字"},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}})]}},{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"success",size:"small",icon:"el-icon-edit"},on:{click:function(t){return e.handleEdit(a.$index,a.row)}}},[e._v("编辑 ")]),t("el-button",{attrs:{type:"danger",size:"small",icon:"el-icon-delete"},on:{click:function(t){return e.handleDelete(a.row)}}},[e._v("删除 ")])]}}])})],1),t("MyEditUserDialog",{attrs:{visible:e.visibleEditDialog,"table-data":e.clickTimeRow},on:{"update:visible":function(t){e.visibleEditDialog=t}}})],1)},x=[],_=(a(7658),a(4161));let k="42.192.221.73",S="8012",$="http",C=-3,M=1e4,L=$+"://"+k+":"+S;var A=a(680);const I="token";function E(){return A.Z.get(I)}function O(e){return A.Z.set(I,e)}function U(){return A.Z.remove(I)}function T(e){const t=_.Z.create({baseURL:L,timeout:M});return t.interceptors.request.use((e=>(e.headers["token"]=E(),e)),(()=>{})),t.interceptors.response.use((e=>e),(e=>e)),t(e)}function F(e,t,a,s=!0){const i=E();(null!==i&&void 0!==i&&""!==i&&"undefined"!==i||!s)&&e.then((e=>{200===e.status&&200===e.data.error_code?t(e.data):e.data.error_code===C?(U(),(0,r.Message)({type:"warning",message:"登录失效，请重新登录！",center:!0}),a(e.data,!0)):((0,r.Message)({type:"error",message:e.data.reason,center:!0}),a(e.data,!1))})).catch((()=>{a(null,!1)}))}const G={post(e){return e.transformRequest=[function(e){let t="";for(let a in e)t+=encodeURIComponent(a)+"="+encodeURIComponent(e[a])+"&";return t=t.substring(0,t.lastIndexOf("&")),t}],e.headers={"Content-Type":"application/x-www-form-urlencoded"},T({...e,method:"POST"})},get(e){return T({...e,method:"GET"})},patch(e){return T({...e,method:"PATCH"})}};var N=G;function z(e,t){return N.post({url:"/myf-bg-api/login",data:{account:e,password:t}})}function R(e){return N.get({url:`/myf-bg-api/info?token=${e}`})}function P(){return N.get({url:"/myf-bg-api/all-user"})}function B(e){return N.post({url:"/myf-bg-api/edit-user",data:e})}function j(e){return N.get({url:`/myf-bg-api/del-user/${e}`})}function Z(){return N.get({url:"/myf-bg-api/all-group"})}function q(e){return N.get({url:`/myf-bg-api/del-group/${e}`})}function H(){return N.get({url:"/myf-puc-api/puc-group-type"})}var J=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",e._g(e._b({attrs:{"modal-append-to-body":!1,title:"编辑用户"},on:{open:e.onOpen,close:e.onClose}},"el-dialog",e.$attrs,!1),e.$listeners),[t("el-form",{ref:"elForm",attrs:{model:e.formData,rules:e.rules,size:"medium","label-width":"100px"}},[t("el-form-item",{attrs:{label:"用户账号"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"不可更改",readonly:"",disabled:!0,clearable:""},model:{value:e.formData.userAccount,callback:function(t){e.$set(e.formData,"userAccount",t)},expression:"formData.userAccount"}})],1),t("el-form-item",{attrs:{label:"年龄"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"不可更改",readonly:"",disabled:!0,clearable:""},model:{value:e.formData.userAge,callback:function(t){e.$set(e.formData,"userAge",t)},expression:"formData.userAge"}})],1),t("el-form-item",{attrs:{label:"密码"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"请输入密码",clearable:"","show-password":""},model:{value:e.formData.userPassword,callback:function(t){e.$set(e.formData,"userPassword",t)},expression:"formData.userPassword"}})],1),t("el-form-item",{attrs:{label:"性别"}},[t("el-radio-group",{attrs:{size:"small"},model:{value:e.formData.userSex,callback:function(t){e.$set(e.formData,"userSex",t)},expression:"formData.userSex"}},e._l(e.fieldSexOptions,(function(a,s){return t("el-radio-button",{key:s,attrs:{label:a.value,disabled:a.disabled,border:""}},[e._v(e._s(a.label)+" ")])})),1)],1),t("el-form-item",{attrs:{label:"用户姓名"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"请输入用户姓名",clearable:""},model:{value:e.formData.userName,callback:function(t){e.$set(e.formData,"userName",t)},expression:"formData.userName"}})],1),t("el-form-item",{attrs:{label:"单位组织"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"请输入单位组织",clearable:""},model:{value:e.formData.userUnit,callback:function(t){e.$set(e.formData,"userUnit",t)},expression:"formData.userUnit"}})],1),t("el-form-item",{attrs:{label:"出生日期"}},[t("el-date-picker",{style:{width:"100%"},attrs:{format:"yyyy-MM-dd","value-format":"yyyy-MM-dd",placeholder:"请选择出生日期",clearable:""},model:{value:e.formData.userBirth,callback:function(t){e.$set(e.formData,"userBirth",t)},expression:"formData.userBirth"}})],1),t("el-form-item",{attrs:{label:"用户介绍"}},[t("el-input",{style:{width:"100%"},attrs:{type:"textarea",placeholder:"请输入用户介绍",maxlength:200,autosize:{minRows:4,maxRows:4}},model:{value:e.formData.userIntroduce,callback:function(t){e.$set(e.formData,"userIntroduce",t)},expression:"formData.userIntroduce"}})],1),t("el-form-item",{attrs:{label:"用户级别"}},[t("el-select",{style:{width:"100%"},attrs:{placeholder:"请选择用户级别",clearable:""},model:{value:e.formData.userGrade,callback:function(t){e.$set(e.formData,"userGrade",t)},expression:"formData.userGrade"}},e._l(e.fieldGradeOptions,(function(e,a){return t("el-option",{key:a,attrs:{label:e.label,value:e.value,disabled:e.disabled}})})),1)],1),t("el-form-item",{attrs:{label:"修改图片"}},[t("el-upload",{staticClass:"avatar-uploader",attrs:{action:"#","show-file-list":!1,"auto-upload":!0,"before-upload":e.beforeAvatarUpload}},[e.image_url?t("img",{staticClass:"up_data_avatar",attrs:{src:e.image_url,alt:""}}):t("i",{staticClass:"el-icon-plus avatar-uploader-icon-up-data"})])],1),t("el-form-item",{attrs:{label:"拉黑"}},[t("el-switch",{attrs:{"active-color":"#DE1D1D"},model:{value:e.formData.userLimit,callback:function(t){e.$set(e.formData,"userLimit",t)},expression:"formData.userLimit"}})],1)],1),t("div",{attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.close}},[e._v("取消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.handleConfirm}},[e._v("确定")])],1)],1)],1)},K=[],Q={name:"MyEditUserDialog",inheritAttrs:!1,components:{},props:{tableData:{type:void 0,default:void 0}},data(){return{image_url:void 0,file_raw:void 0,token:E(),baseAddress:L,formData:{userAccount:void 0,userAge:void 0,userPassword:void 0,userSex:!1,userName:void 0,userUnit:void 0,userBirth:void 0,userIntroduce:void 0,userGrade:0,userLimit:!1},rules:{userAccount:[],userAge:[],userPassword:[],userSex:[],userName:[],userUnit:[],userBirth:[],userIntroduce:[],userGrade:[],userLimit:[]},fieldSexOptions:[{label:"男",value:!0},{label:"女",value:!1}],fieldGradeOptions:[{label:"普通用户",value:0},{label:"管理员",value:1},{label:"ROOT管理员",value:2}]}},computed:{},watch:{},created(){},mounted(){},methods:{onOpen(){this.formData.userPassword="",this.formData.userAccount=this.tableData.userAccount,this.formData.userAge=this.tableData.userAge,this.formData.userSex=this.tableData.userSex,this.formData.userName=this.tableData.userName,this.formData.userUnit=this.tableData.userUnit,this.formData.userBirth=this.tableData.userBirth,this.formData.userIntroduce=this.tableData.userIntroduce,this.formData.userGrade=this.tableData.userGrade,this.formData.userLimit=this.tableData.userLimit,this.image_url=this.baseAddress+this.tableData.userImg+"&token="+this.token},onClose(){this.$refs["elForm"].resetFields(),this.file_raw=void 0,this.image_url=void 0},close(){this.file_raw=void 0,this.image_url=void 0,this.$emit("update:visible",!1)},beforeUpload(e,t,a,s,i=null){let o=!1;e.forEach((function(e){a.type===e&&(o=!0)}));const l=a.size/1024/1024<t;return o?l?(s(a),!0):((0,r.Message)({type:"warning",message:`上传文件大小不能超过 ${t}MB!`,center:!0}),null!=i&&i(a,`上传文件大小不能超过 ${t}MB!`),!1):((0,r.Message)({type:"warning",message:"上传格式暂不支持!",center:!0}),null!=i&&i(a,"上传格式暂不支持!"),!1)},beforeAvatarUpload(e){const t=this;t.beforeUpload(["image/jpeg","image/png","image/webp"],10,e,(function(){t.file_raw=e,t.image_url=window.URL.createObjectURL(e)}))},handleConfirm(){this.$refs["elForm"].validate((e=>{if(!e)return;const t=this.$loading({lock:!0,text:"修改中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});F(B({user_id:this.tableData.id,password:void 0===this.formData.userPassword||null==this.formData.userPassword||""===this.formData.userPassword?"":this.formData.userPassword,sex:this.formData.userSex,name:this.formData.userName,unit:this.formData.userUnit,birth:this.formData.userBirth,introduce:this.formData.userIntroduce,grade:this.formData.userGrade,limit:this.formData.userLimit,image:void 0===this.file_raw||null==this.file_raw||""===this.file_raw?"":this.file_raw}),(()=>{t.close(),this.$bus.$emit("isRefresh",!0),(0,r.Message)({type:"success",message:"修改完成",center:!0}),this.file_raw=void 0}),((e,a)=>{a?this.$bus.$emit("isShowLogin",!0):t.close()})),this.close()}))}}},V=Q,W=(0,v.Z)(V,J,K,!1,null,"19150ccb",null),X=W.exports,Y={name:"UserManagement",components:{MyEditUserDialog:X},data(){return{tableData:void 0,search:"",token:E(),baseAddress:L,visibleEditDialog:!1,clickTimeRow:void 0,previewSrcList:[]}},mounted(){this.refreshData(),this.$bus.$on("isRefresh",(()=>{this.refreshData()}))},methods:{loadImage(e){e.forEach((e=>{this.previewSrcList.push(this.baseAddress+e.userImg+"&token="+this.token)}))},handleEdit(e,t){console.log(e,t),this.clickTimeRow=t,this.visibleEditDialog=!0},handleDelete(e){this.$confirm(`是否删除${e.userAccount}用户`,"删除此用户",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{const t=this.$loading({lock:!0,text:"修改中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});F(j(e.id),(()=>{this.refreshData(),t.close(),(0,r.Message)({type:"success",message:"删除完成",center:!0})}),((e,a)=>{a?this.$bus.$emit("isShowLogin",!0):(t.close(),this.refreshData())}))})).catch((()=>{}))},userLimit(e,t){const a=this.$loading({lock:!0,text:"修改中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});F(B({user_id:e,limit:t}),(()=>{this.refreshData(),a.close(),(0,r.Message)({type:"success",message:"修改完成",center:!0})}),((e,t)=>{t?this.$bus.$emit("isShowLogin",!0):(a.close(),this.refreshData())}))},refreshData(e=!1){F(P(),(t=>{this.tableData=t.result,this.loadImage(t.result),e&&(0,r.Message)({type:"success",message:"刷新完成",center:!0})}),((e,t)=>{t&&this.$bus.$emit("isShowLogin",!0)}))}}},ee=Y,te=(0,v.Z)(ee,D,x,!1,null,"777a2345",null),ae=te.exports,se=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticStyle:{"margin-top":"10px"}}),t("el-button",{attrs:{type:"primary",size:"small",icon:"el-icon-plus"}},[e._v("新增")]),t("el-button",{attrs:{type:"success",size:"small",icon:"el-icon-refresh-left"},on:{click:function(t){return e.refreshData(!0)}}},[e._v("刷新")]),t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData}},[t("el-table-column",{attrs:{width:"40%",align:"center",label:"ID",prop:"id"}}),t("el-table-column",{attrs:{label:"组创建者Id",align:"center",prop:"userId"}}),t("el-table-column",{attrs:{label:"组图像",align:"center",prop:"userImg"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-image",{staticStyle:{width:"26px",height:"26px","border-radius":"4px"},attrs:{src:e.baseAddress+a.row.groupImg+"?token="+e.token,"preview-src-list":e.previewSrcList}})]}}])}),t("el-table-column",{attrs:{align:"center",label:"组名字",prop:"groupName"}}),t("el-table-column",{attrs:{label:"组介绍",width:"100%","show-overflow-tooltip":!0,align:"center",prop:"groupIntroduce"}}),t("el-table-column",{attrs:{align:"center",label:"组类型"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-tag",{attrs:{effect:"light",size:"small"}},[e._v(e._s(a.row.groupType))])]}}])}),t("el-table-column",{attrs:{width:"200px","show-overflow-tooltip":!0,align:"right"},scopedSlots:e._u([{key:"header",fn:function({}){return[t("el-input",{staticStyle:{color:"white"},attrs:{size:"mini",placeholder:"输入关键字"},model:{value:e.search,callback:function(t){e.search=t},expression:"search"}})]}},{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"success",size:"small",icon:"el-icon-edit"},on:{click:function(t){return e.handleEdit(a.$index,a.row)}}},[e._v("编辑 ")]),t("el-button",{attrs:{type:"danger",size:"small",icon:"el-icon-delete"},on:{click:function(t){return e.handleDelete(a.row)}}},[e._v("删除 ")])]}}])})],1),t("MyEditGroupDialog",{attrs:{visible:e.visibleEditDialog,"table-data":e.clickTimeRow},on:{"update:visible":function(t){e.visibleEditDialog=t}}})],1)},re=[],ie=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",e._g(e._b({attrs:{"modal-append-to-body":!1,title:"编辑小组"},on:{open:e.onOpen,close:e.onClose}},"el-dialog",e.$attrs,!1),e.$listeners),[t("el-form",{ref:"elForm",attrs:{model:e.formData,rules:e.rules,size:"medium","label-width":"100px"}},[t("el-form-item",{attrs:{label:"组名"}},[t("el-input",{style:{width:"100%"},attrs:{placeholder:"请输入组名",clearable:""},model:{value:e.formData.groupName,callback:function(t){e.$set(e.formData,"groupName",t)},expression:"formData.groupName"}})],1),t("el-form-item",{attrs:{label:"组介绍"}},[t("el-input",{style:{width:"100%"},attrs:{type:"textarea",placeholder:"请输入组介绍",maxlength:200,autosize:{minRows:4,maxRows:4}},model:{value:e.formData.groupIntroduce,callback:function(t){e.$set(e.formData,"groupIntroduce",t)},expression:"formData.groupIntroduce"}})],1),t("el-form-item",{attrs:{label:"组类型"}},[t("el-select",{style:{width:"100%"},attrs:{placeholder:"请选择组类型",clearable:""},model:{value:e.formData.groupType,callback:function(t){e.$set(e.formData,"groupType",t)},expression:"formData.groupType"}},e._l(e.fieldGradeOptions,(function(e,a){return t("el-option",{key:a,attrs:{label:e.label,value:e.value,disabled:e.disabled}})})),1)],1),t("el-form-item",{attrs:{label:"修改图片"}},[t("el-upload",{staticClass:"avatar-uploader",attrs:{action:"#","show-file-list":!1,"auto-upload":!0,"before-upload":e.beforeAvatarUpload}},[e.image_url?t("img",{staticClass:"up_data_avatar",attrs:{src:e.image_url,alt:""}}):t("i",{staticClass:"el-icon-plus avatar-uploader-icon-up-data"})])],1)],1),t("div",{attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.close}},[e._v("取消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.handleConfirm}},[e._v("确定")])],1)],1)],1)},oe=[],le={name:"MyEditGroupDialog",inheritAttrs:!1,components:{},props:{tableData:{type:void 0,default:void 0}},data(){return{image_url:void 0,file_raw:void 0,token:E(),baseAddress:L,formData:{groupName:void 0,groupIntroduce:void 0,groupType:void 0,groupImg:void 0},rules:{groupName:[],groupIntroduce:[],groupType:[]},fieldGradeOptions:[{label:"课堂组",value:"课堂组"},{label:"会议组",value:"会议组"},{label:"培训组",value:"培训组"}]}},computed:{},watch:{},created(){},mounted(){},methods:{onOpen(){this.formData.groupName=this.tableData.groupName,this.formData.groupIntroduce=this.tableData.groupIntroduce,this.formData.groupType=this.tableData.groupType,this.image_url=this.baseAddress+this.tableData.groupImg+"?token="+this.token,this.getGroupType()},onClose(){this.$refs["elForm"].resetFields(),this.file_raw=void 0,this.image_url=void 0},close(){this.file_raw=void 0,this.image_url=void 0,this.$emit("update:visible",!1)},getGroupType(){F(H(),(e=>{const t=e.result;let a=Array();for(let s=0;s<t.length;s++)a.push({label:t[s],value:t[s]});this.fieldGradeOptions=a}))},beforeUpload(e,t,a,s,i=null){let o=!1;e.forEach((function(e){a.type===e&&(o=!0)}));const l=a.size/1024/1024<t;return o?l?(s(a),!0):((0,r.Message)({type:"warning",message:`上传文件大小不能超过 ${t}MB!`,center:!0}),null!=i&&i(a,`上传文件大小不能超过 ${t}MB!`),!1):((0,r.Message)({type:"warning",message:"上传格式暂不支持!",center:!0}),null!=i&&i(a,"上传格式暂不支持!"),!1)},beforeAvatarUpload(e){const t=this;t.beforeUpload(["image/jpeg","image/png","image/webp"],10,e,(function(){t.file_raw=e,t.image_url=window.URL.createObjectURL(e)}))},handleConfirm(){this.$refs["elForm"].validate((e=>{if(!e)return;const t=this.$loading({lock:!0,text:"修改中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});F(B({user_id:this.tableData.id,password:void 0===this.formData.userPassword||null==this.formData.userPassword||""===this.formData.userPassword?"":this.formData.userPassword,sex:this.formData.userSex,name:this.formData.userName,unit:this.formData.userUnit,birth:this.formData.userBirth,introduce:this.formData.userIntroduce,grade:this.formData.userGrade,limit:this.formData.userLimit,image:void 0===this.file_raw||null==this.file_raw||""===this.file_raw?"":this.file_raw}),(()=>{t.close(),this.$bus.$emit("isRefresh",!0),(0,r.Message)({type:"success",message:"修改完成",center:!0}),this.file_raw=void 0}),((e,a)=>{a?this.$bus.$emit("isShowLogin",!0):t.close()})),this.close()}))}}},ne=le,ue=(0,v.Z)(ne,ie,oe,!1,null,"deab0f08",null),ce=ue.exports,de={name:"GroupManagement",components:{MyEditGroupDialog:ce},data(){return{tableData:void 0,search:"",token:E(),baseAddress:L,visibleEditDialog:!1,clickTimeRow:void 0,previewSrcList:[]}},mounted(){this.refreshData(),this.$bus.$on("isRefresh",(()=>{this.refreshData()}))},methods:{loadImage(e){e.forEach((e=>{this.previewSrcList.push(this.baseAddress+e.groupImg+"?token="+this.token)}))},handleEdit(e,t){console.log(e,t),this.clickTimeRow=t,this.visibleEditDialog=!0},handleDelete(e){this.$confirm(`是否删除名为 "${e.groupName}" 的小组`,"删除此小组",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((()=>{const t=this.$loading({lock:!0,text:"修改中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.7)"});F(q(e.id),(()=>{this.refreshData(),t.close(),(0,r.Message)({type:"success",message:"删除完成",center:!0})}),((e,a)=>{a?this.$bus.$emit("isShowLogin",!0):(t.close(),this.refreshData())}))})).catch((()=>{}))},refreshData(e=!1){F(Z(),(t=>{this.tableData=t.result,this.loadImage(t.result),e&&(0,r.Message)({type:"success",message:"刷新完成",center:!0})}),((e,t)=>{t&&this.$bus.$emit("isShowLogin",!0)}))}}},me=de,he=(0,v.Z)(me,se,re,!1,null,"07ebf26d",null),pe=he.exports,fe=function(){var e=this,t=e._self._c;return t("div",{staticClass:"navbar"},[t("i",{class:e.icon+" container",on:{click:e.navbarClick}}),t("el-dropdown",{staticStyle:{float:"right"},on:{command:e.handleCommand}},[t("div",[t("div",{staticClass:"user-avatar"},[t("img",{attrs:{src:e.baseAddress+e.userImage+"&token="+e.token,alt:""}})]),t("span",{staticClass:"container",staticStyle:{"font-size":"12px"}},[e._v(e._s(e.userName))])]),t("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[t("el-dropdown-item",{attrs:{command:"a"}},[e._v("退出登录")]),t("el-dropdown-item",{attrs:{command:"b"}},[e._v("管理员设置")])],1)],1)],1)},ge=[],be={name:"MyNavbar",data(){return{icon:"el-icon-s-fold",isCollapse:!ke.data().isCollapse,baseAddress:L,visibleEditUserDialog:!1,token:E(),userImage:void 0,userName:void 0}},mounted(){this.$bus.$on("userDataNav",(e=>{this.userImage=e.userImg,this.userName=e.userAccount}))},methods:{navbarClick(){this.isCollapse=!this.isCollapse,this.$bus.$emit("isCollapse",!this.isCollapse),this.icon=this.isCollapse?"el-icon-s-unfold":"el-icon-s-fold"},handleCommand(e){"a"===e&&(U(),this.$bus.$emit("isShowLogin",!0))}}},ve=be,we=(0,v.Z)(ve,fe,ge,!1,null,"d1f41244",null),ye=we.exports,De={name:"MyFrame",components:{MyMain:y,UserManagement:ae,GroupManagement:pe,MyNavbar:ye},data(){return{isShow:"MyMain",isCollapse:!1,selectIndex:"1",mainContainerLeft:200}},mounted(){this.$bus.$on("isCollapse",(e=>{this.logoClick(e)}))},methods:{logoClick(e){this.isCollapse=e,this.mainContainerLeft=this.isCollapse?64:200},handleOpen(e,t){console.log(e,t)},handleClose(e,t){console.log(e,t)}}},xe=De,_e=(0,v.Z)(xe,u,c,!1,null,"029365d6",null),ke=_e.exports,Se=function(){var e=this,t=e._self._c;return t("div",{staticClass:"login"},[t("el-card",{staticClass:"box-card"},[t("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:e.loginForm,rules:e.loginRules}},[t("h3",{staticClass:"title",staticStyle:{color:"white"}},[e._v("考勤APP后台管理系统")]),t("el-form-item",{attrs:{prop:"username"}},[t("el-input",{attrs:{type:"text","auto-complete":"off",placeholder:"账号"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),t("el-form-item",{attrs:{prop:"password"}},[t("el-input",{attrs:{type:"password","auto-complete":"off",placeholder:"密码"},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}})],1),t("el-checkbox",{staticStyle:{margin:"0 0 25px 0"},model:{value:e.loginForm.rememberMe,callback:function(t){e.$set(e.loginForm,"rememberMe",t)},expression:"loginForm.rememberMe"}},[e._v("记住密码")]),t("el-form-item",{staticStyle:{width:"100%"}},[t("el-button",{staticStyle:{width:"100%","margin-top":"30px",height:"45px"},attrs:{loading:e.loading,size:"medium",type:"primary"},on:{click:e.handleLogin}},[e.loading?t("span",[e._v("登 录 中...")]):t("span",[e._v("登 录")])])],1)],1)],1)],1)},$e=[],Ce={name:"MyLogin",data(){return{codeUrl:"",loginForm:{username:"",password:"",rememberMe:!1},loginRules:{username:[{required:!0,trigger:"blur",message:"请输入您的账号"}],password:[{required:!0,trigger:"blur",message:"请输入您的密码"}]},loading:!1}},methods:{handleLogin(){p(this.loginForm.username,this.loginForm.password)||(this.loading=!0,F(z(this.loginForm.username,this.loginForm.password),(e=>{this.loading=!1,O(e.result.loginToken),console.log("-----------  ok"),this.$bus.$emit("isShowLogin",!1)}),(()=>{U(),this.loading=!1,console.log("----------- error"),this.$bus.$emit("isShowLogin",!0)}),!1))}}},Me=Ce,Le=(0,v.Z)(Me,Se,$e,!1,null,"f052cb5a",null),Ae=Le.exports,Ie={name:"App",components:{MyFrame:ke,MyLogin:Ae},data(){return{isShowLogin:!1,bus:void 0}},mounted(){this.$bus.$on("isShowLogin",(e=>{this.isShowLogin=e,this.isShowLogin||this.lon()})),this.lon()},methods:{lon(){const e=E();console.log(">>>>>>>>>>>>>>>>>>>> "+e),null===e||void 0===e||""===e||"undefined"===e?this.isShowLogin=!0:F(R(e),(e=>{this.isShowLogin=!1,this.$bus.$emit("userDataNav",e.result)}),(()=>{U(),this.isShowLogin=!0}))}}},Ee=Ie,Oe=(0,v.Z)(Ee,l,n,!1,null,null,null),Ue=Oe.exports;s["default"].prototype.$echarts=o;const Te=e=>{const t=e.methods.resizeListener;e.methods.resizeListener=function(){window.requestAnimationFrame(t.bind(this))}};Te(r.Table),s["default"].config.productionTip=!1,s["default"].use(i()),new s["default"]({el:"#app",render:e=>e(Ue),beforeCreate(){s["default"].prototype.$bus=this}})},4759:function(e,t,a){e.exports=a.p+"assets/img/app_logo.3be619d7.svg"}},t={};function a(s){var r=t[s];if(void 0!==r)return r.exports;var i=t[s]={id:s,loaded:!1,exports:{}};return e[s](i,i.exports,a),i.loaded=!0,i.exports}a.m=e,function(){a.amdO={}}(),function(){var e=[];a.O=function(t,s,r,i){if(!s){var o=1/0;for(c=0;c<e.length;c++){s=e[c][0],r=e[c][1],i=e[c][2];for(var l=!0,n=0;n<s.length;n++)(!1&i||o>=i)&&Object.keys(a.O).every((function(e){return a.O[e](s[n])}))?s.splice(n--,1):(l=!1,i<o&&(o=i));if(l){e.splice(c--,1);var u=r();void 0!==u&&(t=u)}}return t}i=i||0;for(var c=e.length;c>0&&e[c-1][2]>i;c--)e[c]=e[c-1];e[c]=[s,r,i]}}(),function(){a.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return a.d(t,{a:t}),t}}(),function(){a.d=function(e,t){for(var s in t)a.o(t,s)&&!a.o(e,s)&&Object.defineProperty(e,s,{enumerable:!0,get:t[s]})}}(),function(){a.g=function(){if("object"===typeof globalThis)return globalThis;try{return this||new Function("return this")()}catch(e){if("object"===typeof window)return window}}()}(),function(){a.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)}}(),function(){a.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})}}(),function(){a.nmd=function(e){return e.paths=[],e.children||(e.children=[]),e}}(),function(){a.p=""}(),function(){var e={143:0};a.O.j=function(t){return 0===e[t]};var t=function(t,s){var r,i,o=s[0],l=s[1],n=s[2],u=0;if(o.some((function(t){return 0!==e[t]}))){for(r in l)a.o(l,r)&&(a.m[r]=l[r]);if(n)var c=n(a)}for(t&&t(s);u<o.length;u++)i=o[u],a.o(e,i)&&e[i]&&e[i][0](),e[i]=0;return a.O(c)},s=self["webpackChunkadmin"]=self["webpackChunkadmin"]||[];s.forEach(t.bind(null,0)),s.push=t.bind(null,s.push.bind(s))}();var s=a.O(void 0,[998],(function(){return a(426)}));s=a.O(s)})();
//# sourceMappingURL=app.12783dc6.js.map