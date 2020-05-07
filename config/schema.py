import graphene

import apps.pages.schema


class Query(apps.pages.schema.Query, graphene.ObjectType):
    pass


schema = graphene.Schema(query=Query)
