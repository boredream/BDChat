# 项目地址
主项目：https://github.com/boredream/BDChat  
依赖主model：https://github.com/boredream/bdcodehelper  
服务端代码：https://github.com/boredream/BoreIMCloud  
###### 欢迎 Star和Follow~

> ###### 注意： 
* 网络框架、常用工具类封装在了依赖主model：bdcodehelper中了，是单独放在一个git项目里的，因此需要手动下载然后复制到主项目目录下。
* 服务端代码需要登录LeanCloud才能部署，因此如果需要自定义修改重新部署请参考LeanCloud相关文档自行申请账号替换配置然后部署。

---

# 项目总结

#### 开发周期：2.5周 
7.3 ~ 7.20
（实际开发天数：10 天）

#### 页面：15个
* Splash页面
* 登录页面
* 忘记密码
* 注册页面
* 会话列表（融云）
* 通讯录
* 我的
* 会话页面（融云基础上稍微修改）
* 会话详情
* 成员列表修改页面
* 新的朋友
* 添加好友
* 好友详情
* 修改信息
* 修改昵称

#### 接口：14个
其中云引擎方法5个（服务端需要些代码）

#### 存在的问题
* 类似的页面比如通讯录和添加好友时候的好友列表，不知道咋提取封装更好。
是直接复用同一个页面？同一个Adapter？还是只同一个ViewHolder
* RxJava使用不够熟练
* 数据返回页面如果不在了，怎么处理更好？不太想用RxLifeCycler的那套
* 图片压缩的东西，因为是用到了Glide所以需要Context对象，咋放到presetner里呢~

---

# 复杂的业务分析
最核心的部分其实是会话列表聊天页面啥的，但是融云已经封装好了，这里本着实用的角度就不重复造轮子了，直接使用~

个人觉得最麻烦的点在于好友关系的处理
就是申请添加、接受、新的好友等相关业务上

#### 好友关系设计
服务端保存一个好友关系FriendRelation表，仨字段，srcUser, targetUser, relation
其中relation字段：
* -1 src申请加target好友
* 1 互相为好友关系


【添加好友流程】
情景一，新的添加
1. A通过昵称或其他信息搜索到用户B
2. A调用接口申请添加好友B
3. 服务端先判断好友关系数据库中AB是否有关系
  1. 如果已经是好友，则返回已添加提示
  2. 如果A曾经向B提交过申请，则返回成功申请提示，但是数据库中不再重复添加好友关系
  3. 如果是B已经向A提交过申请，则直接relation=1双方改为好友
  4. 如果双方没关系，则表中插入一条信息 AUser BUser -1，代表A向B发出了好友申请。同时向B发送一条IM系统消息“xxx申请添加您为好友”

情景二，同意
1. B收到A的好友申请，在新的好友中显示
2. 同意添加好友
     服务端接受到B的同意请求后，将A和B的关系修改为好友，即表中的对应数据修改为 AUser BUser 1


【新的好友】
只有两种情况：对方加我了，显示“同意”、同意后显示“已添加”
注意，我申请加别人不在新的好友中显示
所以获取新的好友列表的服务端设计为：
* 查询tarUser是我的所有关系列表数据，且relation=-1代表其他人对当前用户提交的好友申请，然后返回所有的申请用户
* “已添加”情况来源于本地数据库，只有在收到请求且同意后才修改展示（本功能2.0实现）

---

# 展示页面
![登陆.png](http://upload-images.jianshu.io/upload_images/1513977-5fa6712482b66961.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![会话列表.png](http://upload-images.jianshu.io/upload_images/1513977-ae7c6fd8e89fd26a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![会话页面.png](http://upload-images.jianshu.io/upload_images/1513977-151156b357c08089.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![会话详情.png](http://upload-images.jianshu.io/upload_images/1513977-5214f6c2b2369a99.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![发起群聊.png](http://upload-images.jianshu.io/upload_images/1513977-e9b3462fb16706a1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![搜索好友.png](http://upload-images.jianshu.io/upload_images/1513977-cb468e7052815e07.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![新的朋友.png](http://upload-images.jianshu.io/upload_images/1513977-397faea1618fe801.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![详细资料.png](http://upload-images.jianshu.io/upload_images/1513977-b69b9ca22cb87d07.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![我的.png](http://upload-images.jianshu.io/upload_images/1513977-09ff6accc6e12c66.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)
