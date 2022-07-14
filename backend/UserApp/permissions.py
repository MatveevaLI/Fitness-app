from rest_framework import permissions

class IsOwner(permissions.BasePermission):

    def has_object_permission(self, request, view, obj):
        if request.method in permissions.SAFE_METHODS:
            return True

        if request.user == obj.user:
            return True
            
        return False

class IsOwnerOrIsInvited(permissions.BasePermission):

    def has_object_permission(self, request, view, obj):
        
        if request.user.id == obj.user_owner_id or request.user.id == obj.user_invited_id:
            return True
            
        return False


