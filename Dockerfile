# ============================
# 1. Build Stage for Backend (Ktor)
# ============================
FROM amazoncorretto:17-alpine-jdk AS backend-builder

WORKDIR /app

# Copy the Ktor app source code
COPY . .

# Define the build argument for the variant (dev or release)
ARG BUILD_VARIANT=release

# Build the Ktor app
RUN ./gradlew clean buildFatJar -Penv=$BUILD_VARIANT

# ============================
# 2. Build Stage for Frontend (Vue)
# ============================
FROM node:20-alpine AS frontend-builder

WORKDIR /frontend

# Copy only the frontend folder to avoid unnecessary cache invalidation
COPY frontend/SomethingWithSockets_Frontend /frontend

# Install dependencies and build the Vue app
RUN npm install
RUN npm run build


# ============================
# 3. Final Runtime Stage (Minimal Java Image)
# ============================
FROM amazoncorretto:17-alpine

WORKDIR /app

# Define build arguments for the JAR name and version
# Accept build arguments
ARG KTOR_APP_NAME
ENV KTOR_APP_NAME=${KTOR_APP_NAME}
ARG KTOR_APP_VERSION
ENV KTOR_APP_VERSION=${KTOR_APP_VERSION}

# Copy the built Ktor backend artifacts
COPY --from=backend-builder /app/build/libs /app

# Copy the built Vue frontend
COPY --from=frontend-builder /frontend/dist /app/frontend/SomethingWithSockets_Frontend/dist
EXPOSE 8080

CMD java -jar /app/fat.jar
