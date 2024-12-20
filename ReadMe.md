# [  ysuserial-mod  ]

## 编译方式

```bash
bash build.sh install  -Papache-el
bash build.sh clean package -Papache-el

```

## update-2024-12-06

- Add BES Deserialize

```bash
ysu_BESRhino2_EX-BESEcho
```

## update-2024-09-26

 - Add FineReport FineDruidHsqlLocal gadget

```text
ysu_FineDruidHsqlLocal_EX-TomcatEcho

// 配合 JEG/JMG 覆盖大部分中间件回显链
-g FineDruidHsqlLocal -p 'LF-/path/to/echo.class'
```

```bash
mvn install:install-file -Dfile=lib/ejb/res/org.mozilla.rhino_1.6.7.jar  -DgroupId=com.mozilla -DartifactId=rhino -Dversion=1.6.7 -Dpackaging=jar
mvn install:install-file -Dfile=lib/ejb/res/V9.5.2.4703.002.jar  -DgroupId=bes.ejb -DartifactId=sparkpatch -Dversion=V9.5.2.4703.002 -Dpackaging=jar

```

## update-2024-08-27

 - 添加 finereport jackjson gadget

```bash
mvn install:install-file -Dfile=lib/finereport/fine-third-10.0.jar -DgroupId=com.fr -DartifactId=fine-third -Dversion=10 -Dpackaging=jar
```

## update-2024-05-04

 - 添加 `cb 1.6.1` 反序列化链
 - 添加 `cc k3/4/5` 反序列化链

## update-2024-04-27

 - 添加 `Xstream` (CVE-2021-39139) 反序列化

## update-2024-04-23

 - 添加 `Fastjson1` / `Fastjson2` 反序列化链

# TODO
- [ ] CommonsBeanutils1183TongWeb 反序列化链
- [ ] [tongweb 内存马模版（Listen、Filter）](https://github.com/suizhibo/MemShellGene/blob/master/src/main/java/core/memshell/TongWebListenerMemShell.java)
