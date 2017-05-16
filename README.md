# VoxelEngine
A voxel, minecraft styled engine made from LWJGL with a focus on performace and scalability

## Development map
VoxelEngine is far from complete or even achiving an alpha version.

#### Performance
- Use occlusion culling _Partialy_
- Use viewing frustum culling
- ~~Use backface culling~~

#### Chunk 

-2,2,-2, // 0
            -2,-2,-2, // 2
            2,-2,-2, // 2
            2,2,-2, // 3

            -2,2,2, // 4
            -2,-2,2, // 5
            2,-2,2,
            2,2,2,

            2,2,-2,
            2,-2,-2,
            2,-2,2,
            2,2,2,

            -2,2,-2, // 0
            -2,-2,-2,
            -2,-2,2,
            -2,2,2,

            -2,2,2,
            -2,2,-2,
            2,2,-2,
            2,2,2,

            -2,-2,2, // 20
            -2,-2,-2,
            2,-2,-2,
            2,-2,2 // 23