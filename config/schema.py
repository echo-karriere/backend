import graphene

import apps.event.schema
import apps.forms.interest.schema
import apps.pages.schema


class Query(apps.pages.schema.Query, apps.event.schema.Query, graphene.ObjectType):
    pass


class Mutation(apps.forms.interest.schema.Mutation, graphene.ObjectType):
    pass


schema = graphene.Schema(query=Query, mutation=Mutation)
