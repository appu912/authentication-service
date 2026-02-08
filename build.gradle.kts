plugins {
	`java-library`
	id("org.springframework.boot")
	id("com.diffplug.spotless")
}

val springBootVersion: String by project
val bouncyCastleVersion: String by project;

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
	annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.springframework.security:spring-security-crypto")
	implementation("org.bouncycastle:bcprov-jdk18on:${bouncyCastleVersion}")

	implementation("org.postgresql:postgresql")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:testcontainers-postgresql")
}

tasks.test {
	useJUnitPlatform()
	maxParallelForks = Runtime.getRuntime().availableProcessors().div(2) ?: 1
	testLogging {
		events("passed", "skipped", "failed")
	}
}

spotless {
	java {
		target("src/*/java/**/*.java")
		importOrder("java|javax", "", "com.progmatic", "com.progmatic.auth", "\\#")
		removeUnusedImports()
		googleJavaFormat()
		trimTrailingWhitespace()
		endWithNewline()
		formatAnnotations()
	}
}

