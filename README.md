##STL文件解析
去年一个关于3D打印的项目里用到 趁着这几天有时间 弄出个展示STL文件格式的demo 很感谢github上另一个分享者的STLViewer项目，本项目是在其代码的原有基础上进行修改的 ，优化了一些东西，修复了展示卡顿，展示不全，展示绘制三角时法线等等问题，增加了适配屏幕的矫正功能。

###支持
STl 两种文件格式 ASCII格式和二进制格式

###STL格式简介
ASCII格式

ASCII码格式的STL文件逐行给出三角面片的几何信息，每一行以1个或2个关键字开头。
在STL文件中的三角面片的信息单元 facet 是一个带矢量方向的三角面片，STL三维模型就是由一系列这样的三角面片构成。
整个STL文件的首行给出了文件路径及文件名。
在一个 STL文件中，每一个facet由7 行数据组成，
facet normal 是三角面片指向实体外部的法矢量坐标，
outer loop 说明随后的3行数据分别是三角面片的3个顶点坐标，3顶点沿指向实体外部的法矢量方向逆时针排列。[1] 
ASCII格式的STL 文件结构如下：
```xml
明码://字符段意义
solidfilenamestl//文件路径及文件名
facetnormalxyz//三角面片法向量的3个分量值
outerloop
vertexxyz//三角面片第一个顶点坐标
vertexxyz//三角面片第二个顶点坐标
vertexxyz//三角面片第三个顶点坐标
endloop
endfacet//完成一个三角面片定义
 
......//其他facet
 
endsolidfilenamestl//整个STL文件定义结束

```
二进制格式

二进制STL文件用固定的字节数来给出三角面片的几何信息。
文件起始的80个字节是文件头，用于存贮文件名；
紧接着用 4 个字节的整数来描述模型的三角面片个数，
后面逐个给出每个三角面片的几何信息。每个三角面片占用固定的50个字节，依次是:
3个4字节浮点数(角面片的法矢量)
3个4字节浮点数(1个顶点的坐标)
3个4字节浮点数(2个顶点的坐标)
3个4字节浮点数(3个顶点的坐标)个
三角面片的最后2个字节用来描述三角面片的属性信息。
一个完整二进制STL文件的大小为三角形面片数乘以 50再加上84个字节。
二进制:
```xml
UINT8//Header//文件头
UINT32//Numberoftriangles//三角面片数量
//foreachtriangle（每个三角面片中）
REAL32[3]//Normalvector//法线矢量
REAL32[3]//Vertex1//顶点1坐标
REAL32[3]//Vertex2//顶点2坐标
REAL32[3]//Vertex3//顶点3坐标
UINT16//Attributebytecountend//文件属性统计
```
####参考
http://baike.baidu.com/link?url=iTKpXemNdbVroqAXefRWpGQKkMXWAfgkNtIb2SBF80UiaweHOgBNDhCJ8sT4Sf1G_3Xj39ay_xt13Cf7PdBpxK
