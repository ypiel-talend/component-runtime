#!/usr/bin/env bash
#
#  Copyright (C) 2006-2022 Talend Inc. - www.talend.com
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

set -xe

# Parameters:
# $1: docker tag version
main() {
  local tag="${1?Missing tag}"
  local latest="${2:-false}"

  echo ">> Building and pushing component-server:${tag}"
  cd images/component-server-image
  mvn verify dockerfile:build -P ci-tsbi
  local registry_srv="artifactory.datapwn.com/tlnd-docker-dev/talend/common/tacokit/component-server"
  docker tag "talend/common/tacokit/component-server:${tag}" "${registry_srv}:${tag}"
  docker push "${registry_srv}:${tag}"
  if [[ ${latest} == 'true' ]]; then
    docker tag  "${registry_srv}:${tag}" "${registry_srv}:latest"
    docker push "${registry_srv}:latest"
  fi
  echo ">> Building and pushing component-server-vault-proxy:${tag}"
  cd ../component-server-vault-proxy-image
  mvn verify dockerfile:build -P ci-tsbi
  local registry_srv_proxy="artifactory.datapwn.com/tlnd-docker-dev/talend/common/tacokit/component-server-vault-proxy"
  docker tag "talend/common/tacokit/component-server-vault-proxy:${tag}" "${registry_srv_proxy}:${tag}"
  docker push "${registry_srv_proxy}:${tag}"
  if [[ ${latest} == 'true' ]]; then
    docker tag  "${registry_srv_proxy}:${tag}" "${registry_srv_proxy}:latest"
    docker push "${registry_srv_proxy}:latest"
  fi
  #TODO starter and remote-engine-customizer
  cd ../..
}

main "$@"
