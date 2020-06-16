import graphene
from graphene_django_extras import DjangoListObjectField, DjangoObjectType

from .models import Namespace


class NamespaceType(DjangoObjectType):
    class Meta:
        model = Namespace
        description = "hello?"
        filter_fields = ["title", "namespace"]


class NamespaceMutator(graphene.Mutation):
    class Arguments:
        id = graphene.ID()
        title = graphene.String()
        description = graphene.String()

    namespace = graphene.Field(NamespaceType)

    def mutate(self, info, title, description):
        namespace = Namespace.objects.get(pk=id)
        namespace.title = title
        namespace.description = description
        namespace.save()
        return NamespaceMutator(namespace=namespace)


class NamespaceQuery(graphene.ObjectType):
    namespace = graphene.Node.Field(NamespaceType)
    all_namespaces = DjangoListObjectField(NamespaceType)


class NamespaceMutation(graphene.ObjectType):
    update_namespace = NamespaceMutator.Field()
