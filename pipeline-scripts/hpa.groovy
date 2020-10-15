#!/usr/bin/env groovy

def pipeline_id = env.BUILD_ID
def node_label = NODE_LABEL.toString()
def hpa = HPA.toString().toUpperCase()
def property_file_name = "hpa.properties"

println "Current pipeline job build id is '${pipeline_id}'"

// run hpa scalability  test
stage ('hpa_scale_test') {
	if (hpa == "TRUE") {
		currentBuild.result = "SUCCESS"
		node(node_label) {
			// get properties file
			if (fileExists(property_file_name)) {
				println "Looks like the property file already exists, erasing it"
				sh "rm ${property_file_name}"
			}
			// get properties file
			echo "Root Workspace: ${env.ROOT_WORKSPACE}"
			echo "Properties Prefix: ${env.PROPERTIES_PREFIX}"
			println "Current pipeline job build id is '${pipeline_id}'"
			sh "cat ${env.PROPERTIES_PREFIX}/${property_file_name}"
			def hpa_properties = readProperties file: "${env.PROPERTIES_PREFIX}/${property_file_name}"
			def orchestration_host = hpa_properties['ORCHESTRATION_HOST']
			def orchestration_user = hpa_properties['ORCHESTRATION_USER']
			def workload_image = hpa_properties['WORKLOAD_IMAGE']
			def workload_job_node_selector = hpa_properties['WORKLOAD_JOB_NODE_SELECTOR']
			def workload_job_taint = hpa_properties['WORKLOAD_JOB_TAINT']
			def workload_job_privileged = hpa_properties['WORKLOAD_JOB_PRIVILEGED']
			def kubeconfig_file = hpa_properties['KUBECONFIG_FILE']
			def pbench_instrumentation = hpa_properties['PBENCH_INSTRUMENTATION']
			def enable_pbench_agents = hpa_properties['ENABLE_PBENCH_AGENTS']
			def enable_pbench_copy = hpa_properties['ENABLE_PBENCH_COPY']
			def pbench_server = hpa_properties['PBENCH_SERVER']
			def scale_ci_results_token = hpa_properties['SCALE_CI_RESULTS_TOKEN']
			def job_completion_poll_attempts = hpa_properties['JOB_COMPLETION_POLL_ATTEMPTS']
			def snafu_user = hpa_properties['SNAFU_USER']
			def snafu_cluster_name = hpa_properties['SNAFU_CLUSTER_NAME']
			def es_host = hpa_properties['ES_HOST']
			def es_port = hpa_properties['ES_PORT']
			def es_index_prefix = hpa_properties['ES_INDEX_PREFIX']
			def pbench_ssh_private_key_file = hpa_properties['PBENCH_SSH_PRIVATE_KEY_FILE']
			def pbench_ssh_public_key_file = hpa_properties['PBENCH_SSH_PUBLIC_KEY_FILE']
			def hpa_prefix = hpa_properties['HPA_PREFIX']
			def hpa_cleanup = hpa_properties['HPA_CLEANUP']
			def hpa_basename = hpa_properties['HPA_BASENAME']
			def hpa_max_nodes = hpa_properties['HPA_MAX_NODES']
			def hpa_container_image = hpa_properties['HPA_CONTAINER_IMAGE']
			def hpa_runtime = hpa_properties['HPA_RUNTIME']
			def hpa_nodeselector = hpa_properties['HPA_NODESELECTOR']
			def hpa_description = hpa_properties['HPA_DESCRIPTION']
			def hpa_pause = hpa_properties['HPA_PAUSE']
			def hpa_stepsize = hpa_properties['HPA_STEPSIZE']
			def hpa_retries = hpa_properties['HPA_RETRIES']
			def hpa_sleep_time = hpa_properties['HPA_SLEEP_TIME']
			def hpa_traffic_loader_count = hpa_properties['HPA_TRAFFIC_LOADER_COUNT']
			def hpa_max_replicas = hpa_properties['HPA_MAX_REPLICAS']
			def hpa_min_replicas = hpa_properties['HPA_MIN_REPLICAS']
			def hpa_cpu_percent = hpa_properties['HPA_CPU_PERCENT']
			def hpa_deployment_replicas = hpa_properties['HPA_DEPLOYMENT_REPLICAS']
			def hpa_namespace = hpa_properties['HPA_NAMESPACE']
			def hpa_traffic_loader_image = hpa_properties['HPA_TRAFFIC_LOADER_IMAGE']
			def hpa_deployment_resource_request = hpa_properties['HPA_DEPLOYMENT_RESOURCE_REQUEST']
			def hpa_deployment_resource_limit = hpa_properties['HPA_DEPLOYMENT_RESOURCE_LIMIT']
			def hpa_deployment_image = hpa_properties['HPA_DEPLOYMENT_IMAGE']
			def hpa_deployment_name = hpa_properties['HPA_DEPLOYMENT_NAME']
			def hpa_ssh_port = hpa_properties['HPA_SSH_PORT']
			def hpa_parallelism = hpa_properties['HPA_PARALLELISM']
			def hpa_completions = hpa_properties['HPA_COMPLETIONS']
			def ibm_cloud_storage_billing = hpa_properties['IBM_CLOUD_STORAGE_BILLING']
			def ibm_cloud_storage_region = hpa_properties['IBM_CLOUD_STORAGE_REGION']
			def ibm_cloud_storage_zone = hpa_properties['IBM_CLOUD_STORAGE_ZONE']
			
			try {
				hpa_build = build job: 'ATS-SCALE-CI-HPA',
				parameters: [   [$class: 'StringParameterValue', name: 'SNAFU_USER', value: snafu_user],
				[$class: 'StringParameterValue', name: 'SNAFU_CLUSTER_NAME', value: snafu_cluster_name],
				[$class: 'StringParameterValue', name: 'ES_HOST', value: es_host],
				[$class: 'StringParameterValue', name: 'ES_PORT', value: es_port],
				[$class: 'StringParameterValue', name: 'ES_INDEX_PREFIX', value: es_index_prefix],
				[$class: 'StringParameterValue', name: 'ORCHESTRATION_HOST', value: orchestration_host],
				[$class: 'StringParameterValue', name: 'ORCHESTRATION_USER', value: orchestration_user],
				[$class: 'StringParameterValue', name: 'WORKLOAD_IMAGE', value: workload_image ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_NODE_SELECTOR', value: Boolean.valueOf(workload_job_node_selector) ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_TAINT', value: Boolean.valueOf(workload_job_taint)  ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_PRIVILEGED', value: Boolean.valueOf(workload_job_privileged)  ],
				[$class: 'StringParameterValue', name: 'KUBECONFIG_FILE', value: kubeconfig_file ],
				[$class: 'BooleanParameterValue', name: 'PBENCH_INSTRUMENTATION', value: Boolean.valueOf(pbench_instrumentation)  ],
				[$class: 'BooleanParameterValue', name: 'ENABLE_PBENCH_AGENTS', value: Boolean.valueOf(enable_pbench_agents)  ],
				[$class: 'BooleanParameterValue', name: 'ENABLE_PBENCH_COPY', value: Boolean.valueOf(enable_pbench_copy)  ],
				[$class: 'StringParameterValue', name: 'PBENCH_SERVER', value: pbench_server ],
				[$class: 'StringParameterValue', name: 'SCALE_CI_RESULTS_TOKEN', value: scale_ci_results_token ],
				[$class: 'StringParameterValue', name: 'JOB_COMPLETION_POLL_ATTEMPTS', value: job_completion_poll_attempts ],
				[$class: 'StringParameterValue', name: 'PBENCH_SSH_PRIVATE_KEY_FILE', value: pbench_ssh_private_key_file ],
				[$class: 'StringParameterValue', name: 'PBENCH_SSH_PUBLIC_KEY_FILE', value: pbench_ssh_public_key_file ],
				[$class: 'StringParameterValue', name: 'HPA_PREFIX', value: hpa_prefix ],
				[$class: 'BooleanParameterValue', name: 'HPA_CLEANUP', value: Boolean.valueOf(hpa_cleanup)],
				[$class: 'StringParameterValue', name: 'HPA_BASENAME', value: hpa_basename],
				[$class: 'StringParameterValue', name: 'HPA_MAX_NODES', value: hpa_max_nodes],
				[$class: 'StringParameterValue', name: 'HPA_CONTAINER_IMAGE', value: hpa_container_image],
				[$class: 'StringParameterValue', name: 'HPA_RUNTIME', value: hpa_runtime],
				[$class: 'StringParameterValue', name: 'HPA_RETRIES', value: hpa_retries], // start here
				[$class: 'StringParameterValue', name: 'HPA_SLEEP_TIME', value: hpa_sleep_time],
				[$class: 'StringParameterValue', name: 'HPA_TRAFFIC_LOADER_COUNT', value: hpa_traffic_loader_count],
				[$class: 'StringParameterValue', name: 'HPA_MAX_REPLICAS', value: hpa_max_replicas],
				[$class: 'StringParameterValue', name: 'HPA_MIN_REPLICAS', value: hpa_min_replicas],
				[$class: 'StringParameterValue', name: 'HPA_CPU_PERCENT', value: hpa_cpu_percent],
				[$class: 'StringParameterValue', name: 'HPA_DEPLOYMENT_REPLICAS', value: hpa_deployment_replicas],
				[$class: 'StringParameterValue', name: 'HPA_NAMESPACE', value: hpa_namespace], 
				[$class: 'StringParameterValue', name: 'HPA_TRAFFIC_LOADER_IMAGE', value: hpa_traffic_loader_image],
				[$class: 'StringParameterValue', name: 'HPA_DEPLOYMENT_RESOURCE_REQUEST', value: hpa_deployment_resource_request],
				[$class: 'StringParameterValue', name: 'HPA_DEPLOYMENT_RESOURCE_LIMIT', value: hpa_deployment_resource_limit],
				[$class: 'StringParameterValue', name: 'HPA_DEPLOYMENT_IMAGE', value: hpa_deployment_image],
				[$class: 'StringParameterValue', name: 'HPA_DEPLOYMENT_NAME', value: hpa_deployment_name],
				[$class: 'StringParameterValue', name: 'HPA_SSH_PORT', value: hpa_ssh_port],
				[$class: 'StringParameterValue', name: 'HPA_PARALLELISM', value: hpa_parallelism],
				[$class: 'StringParameterValue', name: 'HPA_COMPLETIONS', value: hpa_completions],
				[$class: 'StringParameterValue', name: 'HPA_STEPSIZE', value: hpa_stepsize],
				[$class: 'StringParameterValue', name: 'HPA_PAUSE', value: hpa_pause],
				[$class: 'StringParameterValue', name: 'HPA_DESCRIPTION', value: hpa_description],
				[$class: 'StringParameterValue', name: 'HPA_NODESELECTOR', value: hpa_nodeselector],
				[$class: 'StringParameterValue', name: 'IBM_CLOUD_STORAGE_BILLING', value: ibm_cloud_storage_billing],
				[$class: 'StringParameterValue', name: 'IBM_CLOUD_STORAGE_REGION', value: ibm_cloud_storage_region],
				[$class: 'StringParameterValue', name: 'IBM_CLOUD_STORAGE_ZONE', value: ibm_cloud_storage_zone]]

			} catch ( Exception e) {
				echo "ATS-SCALE-CI-HPA Job failed with the following error: "
				echo "${e.getMessage()}"
				currentBuild.result = "FAILURE"
 				sh "exit 1"
			}
			println "ATS-SCALE-CI-HPA build ${hpa_build.getNumber()} completed successfully"
		}
	}
}
