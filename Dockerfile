# Mavenを使用してアプリケーションをビルド
FROM maven:3.9.9-eclipse-temurin-22 AS builder

# ビルド時の作業ディレクトリを設定
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Mavenでビルド
RUN mvn package -DskipTests

# JDK 22を使用
FROM eclipse-temurin:22-jdk

# 作業ディレクトリを設定
WORKDIR /app

# ビルド済みのJARファイルをコピー
COPY --from=builder /app/target/tabisketch-test-0.0.1-SNAPSHOT.jar /app/app.jar

# 環境変数を受け取る
ARG _DATABASE_URL
ARG _DATABASE_USERNAME
ARG _DATABASE_PASSWORD
ARG _GOOGLE_MAPS_API_KEY
ARG _MAIL_PASSWORD
ARG _MAIL_USERNAME

# 環境変数を設定する
ENV DATABASE_URL=${_DATABASE_URL}
ENV DATABASE_USERNAME=${_DATABASE_USERNAME}
ENV DATABASE_PASSWORD=${_DATABASE_PASSWORD}
ENV GOOGLE_MAPS_API_KEY=${_GOOGLE_MAPS_API_KEY}
ENV MAIL_PASSWORD=${_MAIL_PASSWORD}
ENV MAIL_USERNAME=${_MAIL_USERNAME}

# Spring Bootアプリケーションを起動する
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
