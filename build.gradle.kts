import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "com.mayunfeng"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web")
    // spring websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
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
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}


tasks.withType<JavaCompile>{
    options.encoding = "UTF-8"
    // 控制台中文乱码
    // https://blog.csdn.net/weixin_46196153/article/details/125907615
}

tasks.withType<Test> {
    useJUnitPlatform()
}
