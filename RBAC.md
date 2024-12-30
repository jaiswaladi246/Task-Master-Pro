
### **Updated Steps for Service Account and Role Creation**

1. **Create the Service Account**:
   Define a service account for Jenkins in the `webapps` namespace:
   ```yaml
   apiVersion: v1
   kind: ServiceAccount
   metadata:
     name: jenkins
     namespace: webapps
   ```


2. **Create Role for Namespace-Scoped Permissions**:
   Define a `Role` for namespace-specific resources (e.g., PVCs, Deployments):
   ```yaml
   apiVersion: rbac.authorization.k8s.io/v1
   kind: Role
   metadata:
     name: app-role
     namespace: webapps
   rules:
     - apiGroups:
         - ""
       resources:
         - pods
         - configmaps
         - secrets
         - services
         - persistentvolumeclaims
       verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]
     - apiGroups:
         - apps
       resources:
         - deployments
         - replicasets
       verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]
   ```

3. **Bind the Role to the Service Account**:
   Attach the `Role` to the `jenkins` service account:
   ```yaml
   apiVersion: rbac.authorization.k8s.io/v1
   kind: RoleBinding
   metadata:
     name: app-rolebinding
     namespace: webapps
   roleRef:
     apiGroup: rbac.authorization.k8s.io
     kind: Role
     name: app-role
   subjects:
     - kind: ServiceAccount
       name: jenkins
       namespace: webapps
   ```

4. **Create ClusterRole for Cluster-Scoped Resources**:
   Add a `ClusterRole` for cluster-scoped resources like PVs and StorageClasses:
   ```yaml
   apiVersion: rbac.authorization.k8s.io/v1
   kind: ClusterRole
   metadata:
     name: jenkins-cluster-role
   rules:
     - apiGroups: [""]
       resources:
         - persistentvolumes
       verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]
     - apiGroups: ["storage.k8s.io"]
       resources:
         - storageclasses
       verbs: ["get", "list", "watch"]
   ```

5. **Bind the ClusterRole to the Service Account**:
   Create a `ClusterRoleBinding` to attach the `ClusterRole` to the `jenkins` service account:
   ```yaml
   apiVersion: rbac.authorization.k8s.io/v1
   kind: ClusterRoleBinding
   metadata:
     name: jenkins-cluster-role-binding
   roleRef:
     apiGroup: rbac.authorization.k8s.io
     kind: ClusterRole
     name: jenkins-cluster-role
   subjects:
     - kind: ServiceAccount
       name: jenkins
       namespace: webapps
   ```


### Generate token using service account in the namespace
[Create Token](https://kubernetes.io/docs/reference/access-authn-authz/service-accounts-admin/#:~:text=To%20create%20a%20non%2Dexpiring,with%20that%20generated%20token%20data.)
