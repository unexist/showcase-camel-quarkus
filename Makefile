define JSON_TODO
curl -X 'POST' \
  'http://localhost:8080/todo' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "string",
  "done": true,
  "title": "string"
}'
endef
export JSON_TODO

# Tools
todo:
	@echo $$JSON_TODO | bash

list:
	@curl -X 'GET' 'http://localhost:8080/todo' -H 'accept: */*' | jq .

# Minikube
minikube-install:
	@minikube addons enable registry
	@minikube addons enable metrics-server

minikube-start:
	@minikube start --driver=qemu --cpus=4 --memory=8192m --network socket_vmnet

# Kamel
kamel-install:
	@kamel install --olm=false --force

# Camel
kamel-deploy-rest:
	kamel run --name todo-rest --dev \
		todo-service/src/main/java/dev/unexist/showcase/route/TodoRestRoute.java \
		--save
