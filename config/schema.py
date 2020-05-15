import graphene

import apps.pages.schema
import apps.event.schema


class Query(apps.pages.schema.Query, apps.event.schema.Query, graphene.ObjectType):
    pass


schema = graphene.Schema(query=Query)
