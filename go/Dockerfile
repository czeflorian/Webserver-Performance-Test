# syntax=docker/dockerfile:1

FROM golang:1.16-alpine

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
COPY lorem-ipsum.txt ./
RUN go mod download

COPY *.go ./

RUN go build -o /go-server

EXPOSE 8080

CMD [ "/go-server" ]