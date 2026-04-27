FROM node:20-alpine AS builder
WORKDIR /app

ARG VITE_JAVA_API
ENV VITE_JAVA_API=$VITE_JAVA_API

ARG VITE_GO_RDT
ENV VITE_GO_RDT=$VITE_GO_RDT

COPY package*.json ./
RUN npm install

COPY . .
RUN npm install

COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]