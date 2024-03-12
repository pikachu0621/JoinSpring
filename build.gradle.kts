import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.pkpk"
version = "0.0.4"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/central") }
    maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
    maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { setUrl("https://maven.aliyun.com/repository/public") }
    mavenCentral()
}

dependencies {

    //  ========================spring=====================================
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    // spring websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    // http请求
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    //  ========================spring=====================================


    //  ========================kotlin=====================================
    // kotlin webflux 返回值的协程支持
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // kotlin webflux await的协程支持
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    // kotlin json解析
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // kotlin 反射库
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // kotlin 核心库
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //  ========================kotlin=====================================


    //  ========================其他=====================================
    // mybatis-plus 数据持久层
    implementation("com.baomidou:mybatis-plus-boot-starter:3.4.2")
    // 支持 webp 文件解码
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
    // 基于 mybatis 自动创建数据库表单
    implementation("com.gitee.sunchenbin.mybatis.actable:mybatis-enhance-actable:1.5.0.RELEASE")
    // 配置注解
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // mysql 连接器
    runtimeOnly("mysql:mysql-connector-java")
    // gson 解析 适配性好
    implementation("com.google.code.gson:gson:2.8.9")
    //  ========================其他=====================================


    //  ========================测试=====================================
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //  ========================测试=====================================
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    // 控制台中文乱码
    // https://blog.csdn.net/weixin_46196153/article/details/125907615
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
tasks.withType<Copy> {
    filesMatching("application.properties"){
        include("application.properties")
        expand(project.properties)
    }
}*/
